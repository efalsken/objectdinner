package com.db4o.android;

public interface ServerSetup {
  
                /**
                * the host to be used.
                */
                public String   HOST = "localhost";  
                /**
                * the database file to be used by the server.
                */
                public String   FILE = "server.db4o";
  
                /**
                * the port to be used by the server.
                */
                public int    PORT = 0xdb40;
  
                /**
                * the user name for access control.
                */
                public String   USER = "db4o";
  
                /**
                * the password for access control.
                */
                public String   PASS = "db4o";
}