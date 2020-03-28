import socket
import time
import click
 
striplength = 720
i = 0
index = 0
flag = 1
sleeep = 0.1
R = 30
G = 10
B = 0


UDP_IP = "192.168.0.25"
UDP_PORT = 4210
payload = bytearray(striplength)
for k in range(0,240):
  payload[k*3]=R
  payload[k*3+1]=G
  payload[k*3+2]=B

sock = socket.socket(socket.AF_INET, # Internet
                       socket.SOCK_DGRAM) # UDP
sock.sendto(payload, (UDP_IP, UDP_PORT))

