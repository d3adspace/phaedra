# Phaedra
Youre tired about parsing command line arguments with endless lines of code in your main class? 
Then you will fall in love with phaedra in an instant. Phaedra is supposed to easily parse command
line arguments. It is not really ready to use and still under heavy development.

# Installation / Usage

- Install [Maven](http://maven.apache.org/download.cgi)
- Clone this repo
- Install: ```mvn clean install```

**Maven dependencies**

```xml
<dependency>
    <groupId>de.d3adspace</groupId>
    <artifactId>phaedra</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

# Example

_Parsing:_
```java
Phaedra phaedra = PhaedraFactory.createPhaedra();
		
phaedra.setOptionProvider(PhaedraExampleOptionProvider.class);
PhaedraExampleOptionProvider provider = (PhaedraExampleOptionProvider) phaedra.parse("java -jar server-jar -h PhaedraCommandLineParser -y localhost:8080,localhost:8081,localhost:8082 -s".split(" "));
		
String appName = provider.getAppName();
List<String> hosts = provider.getHosts();
boolean useSSL = provider.isSslEnabled();
```

_Provider:_
```java
public class PhaedraExampleOptionProvider {
	
	@Option(key = "h")
	private String appName = "Phaedra";
	
	@Option(key = "y")
	private List<String> hosts = new ArrayList<>();
	
	@Option(key = "s", needsValue = false)
	private boolean sslEnabled = false;
	
	public String getAppName() {
		return appName;
	}
	
	public List<String> getHosts() {
		return hosts;
	}
	
	public boolean isSslEnabled() {
		return sslEnabled;
	}

```