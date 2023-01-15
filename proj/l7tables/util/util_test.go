package util

import "testing"

func TestOk(t *testing.T) {
	ret := Ok()
	if ret != "OK2s" {
		t.Errorf("Abs(-1) = %s; want 1", ret)
	}
}
