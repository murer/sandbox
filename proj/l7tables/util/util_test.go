package util

import "testing"

func TestOk(t *testing.T) {
	ret := Ok()
	if ret != "OK" {
		t.Errorf("Abs(-1) = %s; want 1", ret)
	}
}

func BenchmarkOk(b *testing.B) {
	for i := 0; i < b.N; i++ {
		Ok()
	}
}
