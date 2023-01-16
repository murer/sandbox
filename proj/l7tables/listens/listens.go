package sockets

type Listens struct {
}

func (me Listens) Listen(proto string, address string) {
}

func Create() *Listens {
	return &Listens{}
}
