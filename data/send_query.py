"""
Send sample query to prediction engine
"""

import predictionio
engine_client = predictionio.EngineClient(url="http://localhost:8000")
print engine_client.send_query({"features": [-0.370344103944531,0.308762580366607,2.29616823667723,0.326180733364224,1.14962499272667,2.17550437372328,2.1921676611964,0.405329835317383,11.1219386037848,13.5510781575918]})
