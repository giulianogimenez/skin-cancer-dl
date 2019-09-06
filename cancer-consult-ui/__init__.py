from flask import Flask, request, send_from_directory

app = Flask(__name__)

@app.route("/")
def home():
    return send_from_directory(".", "index.html")

@app.route("/js/<path:path>")
def js(path):
    return send_from_directory("js", path)

@app.route("/css/<path:path>")
def css(path):
    return send_from_directory("css", path)

app.run(host="0.0.0.0", port=80)
