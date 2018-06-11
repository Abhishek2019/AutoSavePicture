from flask import Flask, request
from flask_restful import Resource, Api
import base64

app = Flask(__name__)

app.secret_key = 'Abhi1234'

api = Api(app)



class Picture(Resource):
    # @jwt_required()
    def post(self):

        req_data = request.get_json()

        f = open('C:/Users/prasanna/PycharmProjects/AutoSavePic_API/picStore/test.txt', 'w')
        f.write(req_data["pic_str"])
        f.close()


        fh = open("C:/Users/prasanna/PycharmProjects/AutoSavePic_API/picStore/imageToSave.jpg", "wb")
        imgdata = base64.b64decode(req_data["pic_str"])
        fh.write(imgdata)
        fh.close()


        return ("Image saved"),201


api.add_resource(Picture,"/picPost")

app.run(host= "192.168.1.107", port = 2935, debug = True)


