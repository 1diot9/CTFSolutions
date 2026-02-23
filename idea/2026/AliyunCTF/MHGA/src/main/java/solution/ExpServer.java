package solution;

import tools.http.Server;

public class ExpServer {
    public static void main(String[] args) {
        LdapRefServer0 ldapRefServer0 = new LdapRefServer0();
        Server server = new Server();
        LdapRefServer1 ldapRefServer1 = new LdapRefServer1();
        HessianServer hessianServer = new HessianServer();

        Thread ldap0Thread = new Thread(ldapRefServer0);
        Thread serverThread = new Thread(server);
        Thread ldap1Thread = new Thread(ldapRefServer1);
        Thread hessianServerThread = new Thread(hessianServer);

        ldap0Thread.start();
        serverThread.start();
        ldap1Thread.start();
        hessianServerThread.start();
    }
}
