import predictionio
import argparse
import csv

def import_events(client, file):
  with open(file, 'r') as f:
    reader = csv.DictReader(f)
    count = 0
    print "Importing data..."
    for row in reader:
      client.create_event(
        event="$set",
        entity_type="data",
        entity_id=str(count),
        properties= {
          "V1" : row["V1"],
          "V2" : row["V2"],
          "V3" : row["V3"],
          "V4" : row["V4"],
          "V5" : row["V5"],
          "V6" : row["V6"],
          "V7" : row["V7"],
          "V8" : row["V8"],
          "V9" : row["V9"],
          "V10" : row["V10"],
          "resp2" : row["resp2"]
        }
      )
      count += 1
    print "%s events are imported." % count

if __name__ == '__main__':
  parser = argparse.ArgumentParser(
  description="Import training data for engine")
  parser.add_argument('--access_key', default='invald_access_key')
  parser.add_argument('--url', default="http://localhost:7070")
  parser.add_argument('--file', default="./data/sample_phrase_data.txt")

  args = parser.parse_args()
  print args

  client = predictionio.EventClient(
    access_key=args.access_key,
    url=args.url,
    threads=5,
    qsize=500)
  import_events(client, args.file)