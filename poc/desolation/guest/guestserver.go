package guest

import (
	"log"
	"net/http"
	"strings"

	"github.com/murer/desolation/message"
	"github.com/murer/desolation/util"
)

func Start() {

	// http.HandleFunc("/", Handle)
	log.Printf("Starting server")
	err := http.ListenAndServe("localhost:0", Handler())
	util.Check(err)
}

func Handler() http.Handler {
	static := "public"
	if !util.FileExists(static) {
		log.Panicf("static dir not found: %s", static)
	}
	mux := http.NewServeMux()
	mux.Handle("/public/", http.StripPrefix("/public", http.FileServer(http.Dir(static))))
	mux.Handle("/", http.HandlerFunc(Handle))
	return mux
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

func messageExtract(r *http.Request) *message.Message {
	// contentType := r.Header.Get("Content-Type")
	// reqBody := ""
	// if strings.HasPrefix(contentType, "application/json") {
	reqBody := util.ReadAllString(r.Body)
	// } else {
	// 	r.ParseForm()
	// 	reqBody = r.Form.Get("msg")
	// }
	// if reqBody == "" {
	// 	log.Fatalf("Message not found: %s", contentType)
	// }
	return message.Decode(reqBody)
}

func HandleCommand(w http.ResponseWriter, r *http.Request) {
	msg := messageExtract(r)
	var ret *message.Message
	if msg.Name == "echo" {
		ret = msg
	} else {
		ret = &message.Message{Name: "unknown", Headers: nil, Payload: ""}
	}
	rid, exists := msg.Headers["rid"]
	if exists {
		ret.Headers["rid"] = rid
	}
	respBody := message.Encode(ret)
	w.Header().Set("Content-Type", "application/json")
	w.Write([]byte(respBody))
}
