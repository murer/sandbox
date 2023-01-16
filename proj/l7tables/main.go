package main

import "github.com/murer/l7tables/listens"

func main() {
	listens := listens.Create()
	defer listens.Close()
	listens.Listen("tcp4", "127.0.0.1:5005")
	listens.Listen("udp4", "127.0.0.1:5006")
}
