package main

import (
	"log"
	"net"
)

func main() {
	listen()
	// wait the listener
	// time.Sleep(1)
	// dial()
}

func handleServer(conn *net.TCPConn) {
	defer conn.Close()
	log.Printf("In conn: %s", conn.RemoteAddr().String())
	conn.Write([]byte("welcome\n"))
}

func listen() {
	dstNet := &net.TCPAddr{IP: net.ParseIP("0.0.0.0"), Port: 9090}
	server, err := net.ListenTCP("tcp4", dstNet)
	if err != nil {
		panic(err)
	}
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

func dial() {
	dstNet := &net.TCPAddr{IP: net.ParseIP("127.0.0.1"), Port: 9090}
	for {
		log.Printf("dialing")
		conn, err := net.DialTCP("tcp", nil, dstNet)
		if err != nil {
			panic(err)
			//return
		}
		log.Printf("new conn %v", conn)
	}
}
