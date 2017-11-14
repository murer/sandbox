Python in-memory gzip

```shell
murer@frostnova:~/proj/frotanet$ python
Python 2.7.6 (default, Jun 22 2015, 17:58:13) 
[GCC 4.8.2] on linux2
Type "help", "copyright", "credits" or "license" for more information.
>>> import gzip
>>> with open('/tmp/a.gz', 'r') as f:
...   c = f.read()
... 
>>> c
'\x1f\x8b\x08\x00\xb9p\nZ\x00\x03KLL\xe4\x02\x00\x95]\xf8w\x04\x00\x00\x00'
>>> import StringIO
>>> buffer = StringIO.StringIO(c)
>>> y = gzip.GzipFile(fileobj=buffer)).read()
  File "<stdin>", line 1
    y = gzip.GzipFile(fileobj=buffer)).read()
                                     ^
SyntaxError: invalid syntax
>>> y = gzip.GzipFile(fileobj=buffer).read()
>>> y
'aaa\n'
>>> output = StringIO.StringIO()
>>> gzip.GzipFile(fileobj=output).write(y)
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
  File "/usr/lib/python2.7/gzip.py", line 224, in write
    raise IOError(errno.EBADF, "write() on read-only GzipFile object")
IOError: [Errno 9] write() on read-only GzipFile object
>>> gzip.GzipFile(fileobj=output, 'w').write(y)
  File "<stdin>", line 1
SyntaxError: non-keyword arg after keyword arg
>>> gzip.GzipFile(fileobj=output, mode='w').write(y)
4
>>> output
<StringIO.StringIO instance at 0x7f75f4512518>
>>> output.getvalue()
'\x1f\x8b\x08\x00\xddq\nZ\x02\xffKLL\xe4\x02\x00\x95]\xf8w\x04\x00\x00\x00'
>>> with open('/tmp/y.gz', 'w') as f:
...   f.write(output.getvalue())
... 
>>> 
murer@frostnova:~/proj/frotanet$ cat /tmp/y.gz | gunzip 
aaa
murer@frostnova:~/proj/frotanet$ diff /tmp/
a.gz                              hsperfdata_murer/                 testNames6356513349115784755.txt  .X11-unix/
cloudz/                           .ICE-unix/                        upd/                              y.gz
.com.google.Chrome.k75TAJ/        .lxterminal-socket:0.0-murer      .X0-lock                          
murer@frostnova:~/proj/frotanet$ diff /tmp/
a.gz                              hsperfdata_murer/                 testNames6356513349115784755.txt  .X11-unix/
cloudz/                           .ICE-unix/                        upd/                              y.gz
.com.google.Chrome.k75TAJ/        .lxterminal-socket:0.0-murer      .X0-lock                          
murer@frostnova:~/proj/frotanet$ diff /tmp/a.gz /tmp/y.gz 
Binary files /tmp/a.gz and /tmp/y.gz differ
murer@frostnova:~/proj/frotanet$ gunzip /tmp/a.gz /tmp/y.gz 
murer@frostnova:~/proj/frotanet$ diff /tmp/a /tmp/y
```
