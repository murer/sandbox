
This is simple to automate, you just need to set the proper debconf configuration for this package.

First install debconf-utils:

```shell
sudo apt install debconf-utils
```

If you've already configured the package, you can read the debconf configuration with:

```shell
debconf-get-selections | grep keyboard-configuration
```

If you haven't configured the package or would like to change your selections, you can do this with:

```shell
dpkg-reconfigure keyboard-configuration
```

Export your selections to a file

```shell
debconf-get-selections | grep keyboard-configuration > selections.conf
```

Copy selections.conf to the target machine and set the selections:

```shell
debconf-set-selections < selections.conf
```

When you install or reconfigure the package, your choices will now be selected automatically.

```shell
dpkg-reconfigure keyboard-configuration -f noninteractive
```
