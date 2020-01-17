# Hex

## Prepare install (live) env

```shell
./cmds/10-install-prepare.sh
```

## Partition

Create 2 partitions with ```gdisk /dev/sdX```:

User ```o``` to create a empty gpt table if necessary

```text
Number  Start (sector)    End (sector)  Size       Code  Name
   1            2048          821247   400.0 MiB   EF00  EFI System
   2          821248       147621887   70.0 GiB    8300  Linux filesystem
```
