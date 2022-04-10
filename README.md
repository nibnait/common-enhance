# 一些Java开发过程中的 常用工具类 集合

# 本 jar 包 已上传到 github maven 仓库
直接导入即可：

```
    <repositories>
        <repository>
            <id>github</id>
            <url>https://raw.github.com/nibnait/java-common/master</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
```

```
    <dependency>
        <groupId>cc.tianbin</groupId>
        <artifactId>java-common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```
目前仅用于个人开发使用，为了方便 我只打了一个 snapshot 版本。不提供 release 版本。  
想用 release 版本，自己 copy 代码，回去自己打。O(∩_∩)O~

---
里面的 json-comparison 可能需要自己手动下载下：
[https://github.com/eBay/json-comparison/issues/3](https://github.com/eBay/json-comparison/issues/3)  
[https://github.com/eBay/json-comparison/packages/68314](https://github.com/eBay/json-comparison/packages/68314)