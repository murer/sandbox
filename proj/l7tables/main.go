package main

func main() {

	a := func() {

	}
	a()

	// kernel.Init()

	// tcp = kernel.addModule(TcpListenerModule())

	// kernel.addModule(ForwardTCPModule())
	// 		.rule(func(conn Conn) {
	//          if .... {
	//             forward("x.x.x.x:nnnn")
	//          }
	//		 })

	// defer kernel.Close()

	// tcp.listen("tcp4", "127.0.0.1:5005")

	// kernel.Wait()
}
