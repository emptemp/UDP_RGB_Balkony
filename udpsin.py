import socket
import time
import click
import math
 
striplength = 720
c = 0
i = 0
index = 0
pi = math.pi

fq = 100
amp = 10
sleep = 0.2

UDP_IP = "192.168.0.25"
UDP_PORT = 4210
payload = bytearray(striplength)
#my_bytes.append(255)
#my_bytes.append(255)
#my_bytes.append(255)

while True:

  #click.echo('direction? [AD] ', nl=False)
  

  #r = (c+1)%255   
  #g = (100 + c + 1)%255   
  #b = (200 + c + 1)%255   
  #for i in range(0,len(payload)):
  for t in range(0,240):
    c = int(math.sin(pi*t/fq+i)*amp+amp)
    payload[index]=c
    payload[index+1]=c/2
    payload[index+2]=0  
    index = (t * 3) % striplength
  #  my_bytes.append(r)
  #  my_bytes.append(g)
  #  my_bytes.append(b)
  i = i + 1
  print "UDP target IP:", UDP_IP
  print "UDP target port:", UDP_PORT

  sock = socket.socket(socket.AF_INET, # Internet
                       socket.SOCK_DGRAM) # UDP
  sock.sendto(payload, (UDP_IP, UDP_PORT))
  time.sleep(sleep)
  payload = bytearray(720)
  
