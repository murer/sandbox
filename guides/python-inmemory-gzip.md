Python in-memory gzip

```python
>>> import gzip
>>> with open('/tmp/a.gz', 'r') as f:
...   c = f.read()
... 
>>> c
'\x1f\x8b\x08\x00\xb9p\nZ\x00\x03KLL\xe4\x02\x00\x95]\xf8w\x04\x00\x00\x00'
>>> import StringIO
>>> buffer = StringIO.StringIO(c)
>>> y = gzip.GzipFile(fileobj=buffer).read()
>>> y
'aaa\n'
>>> output = StringIO.StringIO()
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
```
