= Dark Proxy =

node main.js deamon \
  --deamon_http 3128 \
  --deamon_socks5 1080 \
  --deamon_http /tmp/darkproxy.http.socket \
  --deamon_socks 5000
