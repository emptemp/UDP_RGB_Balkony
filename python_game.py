import socket
import time
import click
 
striplength = 240
i = 0
index = 0
flag = 1

UDP_IP = "192.168.0.21"
UDP_PORT = 4210
payload = bytearray(striplength)
#my_bytes.append(255)
#my_bytes.append(255)
#my_bytes.append(255)

while True:

  #click.echo('direction? [AD] ', nl=False)
  c = click.getchar()
  click.echo()
  if c == 'a':
      click.echo('+++')
      flag = 1
  elif c == 'd':
      click.echo('---')
      flag = -1
  else:
      click.echo('Invalid input :(')
  #r = (c+1)%255   
  #g = (100 + c + 1)%255   
  #b = (200 + c + 1)%255   
  #for i in range(0,len(payload)):
  print index
  payload[index]=10
  payload[index+1]=5
  payload[index+2]=0  
  i = i + (flag * 1)  
  index = (abs(i) * 3) % (striplength*3)
  #  my_bytes.append(r)
  #  my_bytes.append(g)
  #  my_bytes.append(b)

  print "UDP target IP:", UDP_IP
  print "UDP target port:", UDP_PORT

  sock = socket.socket(socket.AF_INET, # Internet
                       socket.SOCK_DGRAM) # UDP
  sock.sendto(payload, (UDP_IP, UDP_PORT))
  #time.sleep(0.01)
  payload = bytearray(900)
  
