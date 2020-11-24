package message

import (
	"log"
	"strconv"

	"github.com/murer/desolation/util"
)

type Message struct {
	Name    string
	Headers map[string]string
	Payload []byte
}

func (m *Message) Get(name string) string {
	ret := m.Headers[name]
	if ret == "" {
		log.Panicf("Message header is required: %s", name)
	}
	return ret
}

func (m *Message) GetInt(name string) int {
	ret, err := strconv.Atoi(m.Get(name))
	util.Check(err)
	return ret
}
