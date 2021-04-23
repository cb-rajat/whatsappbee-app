import os
from twilio.rest import Client
from flask import Flask, request
import requests
from twilio.twiml.messaging_response import MessagingResponse

app = Flask(__name__)
client = Client(os.environ['TWILIO_ACCOUNT_SID'], os.environ['TWILIO_AUTH_TOKEN'])
CB_APP_URL = os.environ['CB_APP_URL']

@app.route('/bot', methods=['POST'])
def bot():
    cb_url = CB_APP_URL
    cb_params = {'from': request.values.get('WaId'), 'message': request.values.get('Body')} 
    print("Posting request to: {} with params: {}".format(cb_url, cb_params))
    r = requests.post(cb_url, json=cb_params)
    print(r.status_code)
    return str({'status': 'ok'})

@app.route('/send', methods=['POST'])
def send():
    print("Processing request: ")
    cdict = {c:request.form.get(c) for c in request.form}
    print(cdict)
    from_whatsapp_number = 'whatsapp:+{}'.format(request.form.get("from_whatsapp_number"))
    to_whatsapp_number = 'whatsapp:+{}'.format(request.form.get("to_whatsapp_number"))
    whatsapp_msg = request.form.get("txt_msg")
    message = client.messages.create(body=whatsapp_msg,
                           from_=from_whatsapp_number,
                           to=to_whatsapp_number)

    return str({'status': 'ok'})
 
if __name__ == '__main__':
    app.run()
