# Deployment

Automatic deployment on push to `main` branch.

## How It Works

Push to `main` → GitHub Actions:
1. Runs tests
2. Builds Docker image
3. Pushes to GitHub Container Registry (GHCR)
4. SSHs to server
5. Updates container with Traefik labels
6. Traefik automatically routes traffic with SSL

**Time:** ~5-7 minutes

---

## Initial Setup

**First time?** Complete setup first: [GITHUB-SETUP.md](GITHUB-SETUP.md)

This covers:
- GitHub Variables & Secrets
- DNS configuration
- Server infrastructure
- SSH keys

---

## Deploy

```bash
git push origin main
```

Watch: `https://github.com/YOUR_USERNAME/YOUR_REPO/actions`

---

## Operations

### View logs

```bash
ssh deploy@hc-02.meimberg.io "docker logs licenses-backend -f"
```

### Restart app

```bash
ssh deploy@hc-02.meimberg.io "cd /srv/projects/licenses-backend && docker compose restart"
```

### Manual redeploy

```bash
ssh deploy@hc-02.meimberg.io "cd /srv/projects/licenses-backend && docker compose pull && docker compose up -d"
```

### SSH into container

```bash
ssh deploy@hc-02.meimberg.io "docker exec -it licenses-backend sh"
```

### Check container status

```bash
ssh deploy@hc-02.meimberg.io "docker ps | grep licenses-backend"
```

### View compose file

```bash
ssh deploy@hc-02.meimberg.io "cat /srv/projects/licenses-backend/docker-compose.yml"
```

---

## Environment Variables

The following environment variables must be set in GitHub Variables/Secrets:

### GitHub Variables
- `SERVER_HOST` - Server hostname (e.g., `hc-02.meimberg.io`)
- `SERVER_USER` - SSH user (default: `deploy`)
- `APP_DOMAIN` - Domain for the application (e.g., `licenses-backend.meimberg.io`)
- `DB_USERNAME` - Database username (optional, defaults to `licenses`)

### GitHub Secrets
- `SSH_PRIVATE_KEY` - Private SSH key for deployment user
- `DB_PASSWORD` - Database password (secure password)

The `.env` file is automatically created on the server during deployment from these secrets. No manual setup required.

---

## Database

The production setup includes a MySQL 8.0 database container. Data is persisted in a Docker volume.

### Backup

```bash
# Backup database
ssh deploy@hc-02.meimberg.io "docker exec licenses-backend-db mysqldump -u licenses -p licenses > /srv/backups/licenses-backend-$(date +%Y%m%d).sql"
```

### Restore

```bash
# Restore database
ssh deploy@hc-02.meimberg.io "docker exec -i licenses-backend-db mysql -u licenses -p licenses < /srv/backups/licenses-backend-YYYYMMDD.sql"
```

---

## Test Data

**Important:** Test data (seed migrations) are **NOT** imported in production.

- **Dev/Test:** Seed data from `db/migration/dev/` is automatically loaded
- **Production:** Only schema migrations from `db/migration/` are applied, no seed data

This ensures production starts with a clean database.

---

## Troubleshooting

### Container not starting

```bash
# View logs
ssh deploy@hc-02.meimberg.io "docker logs licenses-backend"

# View database logs
ssh deploy@hc-02.meimberg.io "docker logs licenses-backend-db"

# View full compose logs
ssh deploy@hc-02.meimberg.io "cd /srv/projects/licenses-backend && docker compose logs"
```

### Database connection issues

```bash
# Check database container
ssh deploy@hc-02.meimberg.io "docker ps | grep licenses-backend-db"

# Check database logs
ssh deploy@hc-02.meimberg.io "docker logs licenses-backend-db"

# Test database connection
ssh deploy@hc-02.meimberg.io "docker exec licenses-backend-db mysql -u licenses -p -e 'SELECT 1'"
```

### SSL issues (502/503 errors)

```bash
# Check Traefik logs
ssh root@hc-02.meimberg.io "docker logs traefik | grep licenses-backend"

# Check Traefik dashboard (if enabled)
# Usually at traefik.yourdomain.com

# Verify labels are correct
ssh deploy@hc-02.meimberg.io "docker inspect licenses-backend | grep -A 10 Labels"
```

### DNS check

```bash
# Check DNS resolution
dig licenses-backend.meimberg.io +short

# Should return server IP
# If empty, DNS not configured or not propagated yet (wait up to 24h)
```

### Flyway migration issues

```bash
# Check application logs for Flyway errors
ssh deploy@hc-02.meimberg.io "docker logs licenses-backend | grep -i flyway"

# Check database migration history
ssh deploy@hc-02.meimberg.io "docker exec licenses-backend-db mysql -u licenses -p -e 'SELECT * FROM flyway_schema_history ORDER BY installed_rank DESC LIMIT 10' licenses"
```

---

## Health Checks

The application exposes health endpoints via Spring Boot Actuator:

- Health: `https://licenses-backend.meimberg.io/actuator/health`
- Info: `https://licenses-backend.meimberg.io/actuator/info`

---

## API Documentation

Once deployed, access API documentation at:

- Swagger UI: `https://licenses-backend.meimberg.io/swagger-ui/index.html`
- OpenAPI JSON: `https://licenses-backend.meimberg.io/v3/api-docs`

