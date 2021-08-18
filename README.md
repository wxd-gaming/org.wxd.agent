# org.example.agent

#### 介绍
热更新，热加载项目组

spring 项目打包请抛弃spring plugin打包方式，否者热更很麻烦

<plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <configuration>
                    <finalName>${finalName}</finalName>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                            <mainClass>xxx.xxxx</mainClass>
                        </manifest>
                        <manifestEntries>
                            <!--加入本地包引用-->
                            <Class-Path>lib/org.example.agent.jar</Class-Path>
                        </manifestEntries>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <configuration>
                    <encoding>UTF-8</encoding>
                    <resources>
                        <resource>
                            <!--引入本地包-->
                            <directory>${basedir}/src/main/lib</directory>
                            <targetPath>${project.build.directory}/lib</targetPath>
                            <includes>
                                <include>*.jar</include>
                            </includes>
                        </resource>
                        <resource>
                            <!--资源文件夹-->
                            <directory>${basedir}/src/main/resources</directory>
                        </resource>
                    </resources>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy-dependencies</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.directory}/lib</outputDirectory>
                            <excludeTransitive>false</excludeTransitive>
                            <stripVersion>false</stripVersion>
                            <includeScope>runtime</includeScope>
                        </configuration>
                    </execution>
                </executions>
            </plugin>