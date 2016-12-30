import darkproxy

def _main():
    proxy = darkproxy.DarkProxy(port=8000)
    proxy.start()
    try:
        proxy.serve()
    finally:
        proxy.stop()

if __name__ == "__main__":
    _main()
