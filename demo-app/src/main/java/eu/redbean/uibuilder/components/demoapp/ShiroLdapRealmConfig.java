package eu.redbean.uibuilder.components.demoapp;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.ldap.DefaultLdapRealm;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration()
public class ShiroLdapRealmConfig {

    @Bean()
    public LdapContextFactory ldapContextFactory() {
        JndiLdapContextFactory contextFactory = new JndiLdapContextFactory();
        contextFactory.setUrl("ldap://localhost:389");
        return contextFactory;
    }

    @Bean()
    public DefaultLdapRealm realm(LdapContextFactory contextFactory) {
        DefaultLdapRealm realm = new DefaultLdapRealm();
        realm.setContextFactory(contextFactory);
        realm.setUserDnTemplate("cn={0},ou=users,dc=example,dc=com");
        return realm;
    }

    @Bean()
    public DefaultSecurityManager defaultSecurityManager(Realm realm) {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }
}
