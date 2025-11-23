# Database Migrations

Database schema is managed by **Flyway**, not JPA/Hibernate. Migrations run automatically on every application startup.

## How It Works

### Automatic Migration on Startup

1. **Application starts** → Spring Boot initializes Flyway
2. **Flyway checks** `flyway_schema_history` table for applied migrations
3. **Flyway runs** any new migration files found in `src/main/resources/db/migration/`
4. **Application continues** startup only after all migrations are applied

### JPA Role

JPA/Hibernate is configured with `ddl-auto: validate`:
- ✅ **Validates** that entity classes match the database schema
- ❌ **Does NOT** create or modify tables
- ❌ **Does NOT** auto-generate schema

**Flyway is the single source of truth** for database structure.

## Initial Setup

On first deployment:
- Flyway creates `flyway_schema_history` table (if needed, with `baseline-on-migrate: true` in prod)
- Runs `V1__init.sql` to create all tables
- Records migration in history table

## Adding New Migrations

When you need to change the database schema:

### 1. Create Migration File

Create a new SQL file in `src/main/resources/db/migration/` with naming pattern:

```
V{version}__{description}.sql
```

Examples:
- `V2__add_created_at_index.sql`
- `V3__add_user_preferences_table.sql`
- `V4__modify_assignments_table.sql`

**Important:** Version numbers must be sequential and unique.

### 2. Write SQL Migration

```sql
-- V2__add_created_at_index.sql
CREATE INDEX idx_products_created_at ON products(created_at);
```

### 3. Commit and Deploy

```bash
git add src/main/resources/db/migration/V2__add_created_at_index.sql
git commit -m "Add index on products.created_at"
git push origin main
```

### 4. Automatic Application

On next deployment:
1. GitHub Actions builds new Docker image (includes new migration)
2. Server pulls new image
3. Container starts → Flyway detects new migration
4. Flyway runs migration automatically
5. Application starts successfully

## Migration Best Practices

### ✅ DO

- Use descriptive names: `V2__add_user_email_index.sql`
- Test migrations locally first
- Make migrations idempotent when possible (use `IF NOT EXISTS`, `ON DUPLICATE KEY UPDATE`)
- Keep migrations small and focused
- Always test rollback scenarios

### ❌ DON'T

- Don't modify existing migration files (create new ones instead)
- Don't skip version numbers
- Don't use JPA `ddl-auto: create` or `update` in production
- Don't manually edit database schema (always use migrations)

## Migration File Locations

- **Schema migrations** (all environments): `src/main/resources/db/migration/`
- **Seed data** (dev/test only): `src/main/resources/db/migration/dev/`

Production only runs migrations from `db/migration/`, not from `db/migration/dev/`.

## Checking Migration Status

### View Applied Migrations

```bash
# On server
ssh deploy@hc-02.meimberg.io "docker exec licenses-backend-db mysql -u licenses -p -e 'SELECT * FROM flyway_schema_history ORDER BY installed_rank DESC' licenses"
```

### Check Application Logs

```bash
# View Flyway migration logs
ssh deploy@hc-02.meimberg.io "docker logs licenses-backend | grep -i flyway"
```

## Troubleshooting

### Migration Fails on Startup

If a migration fails:
1. Application won't start (fail-fast)
2. Check logs: `docker logs licenses-backend`
3. Fix migration SQL
4. Redeploy

### Schema Out of Sync

If JPA validation fails (schema doesn't match entities):
1. Check error message for specific mismatch
2. Create migration to fix schema
3. Redeploy

### Rollback

Flyway doesn't support automatic rollbacks. To rollback:
1. Create a new migration that reverses the change
2. Deploy the rollback migration

Example:
```sql
-- V5__rollback_add_index.sql
DROP INDEX IF EXISTS idx_products_created_at ON products;
```

## Example: Adding a New Column

```sql
-- V2__add_user_phone.sql
ALTER TABLE users 
ADD COLUMN phone VARCHAR(20) NULL AFTER email;

CREATE INDEX idx_users_phone ON users(phone);
```

After deployment, the column is automatically added to production database.

## Summary

- ✅ **Automatic**: Migrations run on every startup
- ✅ **Versioned**: All changes tracked in `flyway_schema_history`
- ✅ **Safe**: Application won't start if migrations fail
- ✅ **Reproducible**: Same migrations run in dev, test, and prod
- ✅ **No manual steps**: Just commit migration files and deploy

