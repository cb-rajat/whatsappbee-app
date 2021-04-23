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
    cb_url = CB_APP_URL#"http://cb-rajat.in.ngrok.io/message"
    cb_params = {'from': request.values.get('WaId'), 'message': request.values.get('Body')}
    r = requests.post(cb_url, json=cb_params)
    print(r.status_code)
    return str({'status': 'ok'})
#
#    incoming_msg = request.values.get('Body', '').lower()
#    resp = MessagingResponse()
#    msg = resp.message()
#    responded = False
#    if 'quote' in incoming_msg:
#        # return a quote
##        r = requests.get('https://api.quotable.io/random')
##        if r.status_code == 200:
##            data = r.json()
#        quote = "My quote for you Tadaa..."
#        msg.body(quote)
#        responded = True
#    if 'cat' in incoming_msg:
#        # return a cat pic
#        msg.media('https://cataas.com/cat')
#        responded = True
#    if not responded:
#        msg.body('I only know about famous quotes and cats, sorry!')
#    return str(resp)

@app.route('/send', methods=['POST'])
def send():
    from_whatsapp_number = request.form.get("from_whatsapp_number")
    to_whatsapp_number = request.form.get("to_whatsapp_number")
    whatsapp_msg = request.form.get("txt_msg")
    message = client.messages.create(body=whatsapp_msg,
#                           media_url='https://cataas.com/cat',
                           from_=from_whatsapp_number,
                           to=to_whatsapp_number)

    return str({'status': 'ok'})
 
if __name__ == '__main__':
    app.run()
