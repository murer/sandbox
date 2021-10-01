# sonar-sa

```shell
    docker run --name sonar-sa --rm -it \
        -p 9000:9000 \
        -v "$(pwd):/opt/sonar-sa/src" \
        murer/sonar-sa
```