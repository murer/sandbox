#!/bin/bash -xe

if [ "x$1" == "xweb" ]; then
	mkdir -p src/main/webapp/WEB-INF || true
	if [ ! -f src/main/webapp/WEB-INF/web.xml ]; then 
		cat > src/main/webapp/WEB-INF/web.xml <<-EOF
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.5" xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">

</web-app>
		EOF
	fi
fi

mkdir -p src/main/java src/main/resources src/test/java src/test/resources || true

