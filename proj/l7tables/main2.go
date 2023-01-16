package main

import (
	"io"
	"log"
	"net"
	"time"

	"github.com/murer/l7tables/util"
)

func main() {
	util.Ok()
	listen()
	dial()
	log.Printf("Wait")
	time.Sleep(3 * time.Second)
}

func handleServer(conn *net.TCPConn) {
	defer func() {
		log.Printf("In conn close: %s", conn.RemoteAddr().String())
		conn.Close()
	}()
	// conn.SetLinger(1)
	log.Printf("In conn: %s", conn.RemoteAddr().String())
	conn.Write([]byte("welcome\n"))
	conn.CloseWrite()
	io.ReadAll(conn)
}

func handleAccept(server *net.TCPListener) {
	defer server.Close()
	log.Printf("Server created")
	for {
		conn, err := server.AcceptTCP()
		if err != nil {
			panic(err)
		}
		go handleServer(conn)
	}
}

func listen() {
	dstNet := &net.TCPAddr{IP: net.ParseIP("0.0.0.0"), Port: 9090}
	server, err := net.ListenTCP("tcp4", dstNet)
	if err != nil {
		panic(err)
	}
	go handleAccept(server)
}

func dial() {
	dstNet := &net.TCPAddr{IP: net.ParseIP("127.0.0.1"), Port: 9090}
	log.Printf("In conn")
	conn, err := net.DialTCP("tcp", nil, dstNet)
	if err != nil {
		panic(err)
		//return
	}
	defer func() {
		log.Printf("Out conn close")
		conn.Close()
	}()
	data, err := io.ReadAll(conn)
	if err != nil {
		panic(err)
	}
	log.Printf("Data: %v", string(data))
}
