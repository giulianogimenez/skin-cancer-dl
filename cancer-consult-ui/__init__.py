from flask import Flask, request, send_from_directory

app = Flask(__name__)

@app.route("/")
def home():
    return send_from_directory(".", "index.html")

@app.route("/js/<path:path>")
def js(path):
    return send_from_directory("js", path)

@app.route("/imgs/<path:path>")
def imgs(path):
    return send_from_directory("imgs", path)

@app.route("/fontawesome-free-5.10.2-web/<path:path>")
def fontawesome(path):
    return send_from_directory("fontawesome-free-5.10.2-web", path)

@app.route("/css/<path:path>")
def css(path):
    return send_from_directory("css", path)

app.run(host="0.0.0.0", port=80)