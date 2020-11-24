package guest_test

import (
	"bytes"
	"net/http"
	"net/http/httptest"
	"net/url"
	"testing"

	"github.com/murer/desolation/guest"
	"github.com/murer/desolation/message"
	"github.com/murer/desolation/util"
	"github.com/stretchr/testify/assert"
)

func TestVersion(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(guest.Handle))
	defer server.Close()
	t.Logf("URL: %s", server.URL)
	resp, err := http.Get(server.URL + "/api/version.txt")
	util.Check(err)
	defer resp.Body.Close()
	assert.Equal(t, 200, resp.StatusCode)
	assert.Equal(t, "text/plain; charset=UTF-8", resp.Header.Get("Content-Type"))
	assert.Equal(t, util.Version, util.ReadAllString(resp.Body))
}

func TestUnknown(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(guest.Handle))
	defer server.Close()
	t.Logf("URL: %s", server.URL)

	msg := &message.Message{
		Name:    "hfewi",
		Headers: map[string]string{"foo": "1", "bar": "2"},
		Payload: "bXVyZXI=",
	}
	resp, err := http.Post(server.URL+"/api/command", "application/json", bytes.NewReader([]byte(message.Encode(msg))))
	util.Check(err)
	assert.Equal(t, 200, resp.StatusCode)
	assert.Equal(t, "application/json", resp.Header.Get("Content-Type"))
	assert.Equal(t, &message.Message{Name: "unknown", Headers: nil, Payload: ""}, message.Decode(util.ReadAllString(resp.Body)))
}

func TestEchoJson(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(guest.Handle))
	defer server.Close()
	t.Logf("URL: %s", server.URL)

	msg := &message.Message{
		Name:    "echo",
		Headers: map[string]string{"foo": "1", "bar": "2"},
		Payload: "bXVyZXI=",
	}
	resp, err := http.Post(server.URL+"/api/command", "application/json", bytes.NewReader([]byte(message.Encode(msg))))
	util.Check(err)
	assert.Equal(t, 200, resp.StatusCode)
	assert.Equal(t, "application/json", resp.Header.Get("Content-Type"))
	assert.Equal(t, msg, message.Decode(util.ReadAllString(resp.Body)))
}

func TestEchoForm(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(guest.Handle))
	defer server.Close()
	t.Logf("URL: %s", server.URL)

	msg := &message.Message{
		Name:    "echo",
		Headers: map[string]string{"foo": "1", "bar": "2"},
		Payload: "bXVyZXI=",
	}
	resp, err := http.PostForm(server.URL+"/api/command", url.Values{
		"msg": {message.Encode(msg)},
	})
	util.Check(err)
	assert.Equal(t, 200, resp.StatusCode)
	assert.Equal(t, "application/json", resp.Header.Get("Content-Type"))
	assert.Equal(t, msg, message.Decode(util.ReadAllString(resp.Body)))
}
