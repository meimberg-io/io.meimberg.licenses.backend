package io.meimberg.licenses.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class QuotedNamingStrategy extends PhysicalNamingStrategyStandardImpl {
  @Override
  public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
    if (name == null) {
      return null;
    }
    String text = name.getText();
    // Quote the identifier with backticks for MySQL
    return Identifier.toIdentifier("`" + text + "`", name.isQuoted());
  }
}

