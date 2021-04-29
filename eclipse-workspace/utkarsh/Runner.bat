SET M2_HOME=C:\apache-maven-3.6.3
SET path=C:\apache-maven-3.6.3\bin:%path%;

cd C:\bin\REPO\JavaProjects\eclipse-workspace\utkarsh\
C:\apache-maven-3.6.3\bin\mvn exec:java -Dexec.mainClass="utkarsh.SiteScrapper"   -e