package guest

import (
	"net/http"
	"os"

	"github.com/murer/desolation/message"
)

var out = os.Stdout
var in = os.Stdin

func HandleCommandWrite(m *message.Message, w http.ResponseWriter, r *http.Request) *message.Message {
	payload := m.PayloadDecode()
	out.Write(payload)
	return &message.Message{Name: "ok", Headers: map[string]string{}, Payload: ""}
}
