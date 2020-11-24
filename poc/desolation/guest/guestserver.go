package guest

import (
	"log"
	"net/http"
	"strings"

	"github.com/murer/desolation/message"
	"github.com/murer/desolation/util"
)

func Start() {
	http.HandleFunc("/", Handle)
	log.Printf("Starting server")
	err := http.ListenAndServe("localhost:0", nil)
	util.Check(err)
}

func Handle(w http.ResponseWriter, r *http.Request) {
	log.Printf("Access: %s %s %s", r.RemoteAddr, r.Method, r.URL)
	if r.Method == "GET" && r.URL.Path == "/api/version.txt" {
		util.RespText(w, util.Version)
	} else if r.Method == "POST" && r.URL.Path == "/api/command" {
		HandleCommand(w, r)
	} else if strings.HasPrefix(r.URL.Path, "/self/") {
	} else if r.Method == "POST" {
	} else {
		http.NotFound(w, r)
	}
}

func HandleCommand(w http.ResponseWriter, r *http.Request) {
	contentType := r.Header.Get("Content-Type")
	reqBody := ""
	if strings.HasPrefix(contentType, "application/json") {
		reqBody = util.ReadAllString(r.Body)
	} else {
		r.ParseForm()
		reqBody = r.Form.Get("msg")
	}
	if reqBody == "" {
		log.Fatalf("Message not found: %s", contentType)
	}
	msg := message.Decode(reqBody)
	var ret *message.Message
	if msg.Name == "echo" {
		ret = msg
	} else {
		ret = &message.Message{Name: "unknown", Headers: nil, Payload: ""}
	}
	respBody := message.Encode(ret)
	w.Header().Set("Content-Type", "application/json")
	w.Write([]byte(respBody))
}
