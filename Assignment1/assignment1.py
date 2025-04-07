from flask import Flask

app = Flask(__name__)

@app.route('/')
def sayHelloWorld():
    return "Hello World\n"

@app.route('/name/<name>')
def sayHelloName(name):
    return "Hello " + name + "\n"
