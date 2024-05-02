from socket import *
import os
import threading
import hashlib

serverPort =  12000

# TCP Socket
serverSocket = socket(AF_INET, SOCK_STREAM)

# Bind the IP and Port to the server
serverSocket.bind(('', serverPort))

# Server is listening, server is waiting for a connection
serverSocket.listen(5)
print('The server is ready to receive')

# Define the directory where uploaded files will be saved
UPLOAD_DIR = "uploads/"

#function to handle a client connection
def handle_client(connectionSocket,addr):
    print(addr[0], "has joined the server")
    while True:
        # Receive the client's request
       
        msg = connectionSocket.recv(1024).decode()
        header = msg.split("/")

        #print(header[0], "\n",header[1])
        # Handle the request
        if header[0] == "UPLOAD":
            receive_file(connectionSocket,addr,header[1])
        elif header[0] == "DOWNLOAD":
            send_file(connectionSocket,addr,header[1])
        elif header[0]=="LIST":
            list_file_names()
        elif header[0] == "QUIT":
            # If the client sends the QUIT command, close the connection
            connectionSocket.close()
            print(addr[0], "has left the server")
            break
        else:
            # If the client sends an invalid command, send an error message
            connectionSocket.sendto("Invalid command".encode(), addr)

#function that receives a file from the client and stores.
file_lock = threading.Lock()
def receive_file(connectionSocket, addr,file_name):
    os.makedirs(UPLOAD_DIR,exist_ok=True)
    msg = connectionSocket.recv(1024).decode()
    #print(file_name)
    file_data = msg.split("/")
    new_file_name = check_duplicates(file_name)
    add_file_name(new_file_name, file_data[0])
    file_size = int(file_data[1])
    # Will create a directory for uploaded files if it does not exist
    os.makedirs(UPLOAD_DIR, exist_ok=True)
    path = os.path.join(UPLOAD_DIR, new_file_name)
    file_lock.acquire()
    file = open(path, "wb")
    file_bytes = 0
    while file_bytes < file_size:
        data = connectionSocket.recv(1024)
        #print(file_bytes)
        file_bytes += len(data)
        file.write(data)
    file.close()
    file_lock.release()
    if(file_data[2]==check_sum(path)):
        #print(f"{check_sum(new_file_name)}/{file_data[2]}")
        print(f"{new_file_name} was uploaded successful")
        connectionSocket.send(f"***********{new_file_name} was successfully uploaded***************".encode())
    else:
        print(f"{check_sum(path)}/{file_data[2]}")
        print("file was unsuccessful")
        connectionSocket.send("***********file failed uploaded***************".encode())

#function to send a file to a client
def send_file(connectionSocket, addr, file_name):
    #Check if the files is open or protected
    if check_protection(file_name)==True:
        #if open than sends files to client
        path = os.path.join(UPLOAD_DIR, file_name)
        file = open(path, "rb")
        file_size = os.path.getsize(file_name)
        file_size = str(file_size)
        connectionSocket.send(f"OPEN/{file_size}/{check_sum(file_name)}".encode())
        data = file.read()
        connectionSocket.sendall(data)
        file.close()
    else:
    #Tells client that it is protected and must ask for a password
        connectionSocket.send(f"PROTECTED/0/0".encode())
        password = connectionSocket.recv(1024).decode()
        #print(password)
        #checks if the password is correct
        if check_password(file_name, password) ==True:
            #if the password is correct than it will send file
            path = os.path.join(UPLOAD_DIR, file_name)
            file = open(path, "rb")
            file_size = os.path.getsize(file_name)
            file_size = str(file_size)
            connectionSocket.send(f"CORRECT/{file_size}/{check_sum(file_name)}".encode())
            data = file.read()
            connectionSocket.sendall(data)
            file.close()
            #else sends error message
        else:
            connectionSocket.send(f"INCORRECT/0".encode())

#function that checks if the file_name is open or closed
def check_protection(file_name):
    with open("access.txt", 'r') as file:
        for line in file:
                line = line.strip().split('/')
                if line[0] == file_name:
                    if len(line) == 1:
                        # file does not have a password
                        return True
                    else:
                        return False

#function that checks if the password is correct or incorrect
def check_password(file_name, password):
    with open("access.txt", 'r') as file:
        for line in file:
            line = line.strip().split('/')
            if line[0] == file_name:
                if line[1] == password:
                    # password is correct
                    return True
                else:
                    #password is incorrect
                    return False
                #file not found
        return False

#function that adds the file_name and password to file
def add_file_name(file_name, password):
    if os.path.exists("access.txt"):
        # append the file_name and password to the file
        file = open("access.txt", "a")
        if password =="":
            file.write(f"{file_name}\n")
        else:
            file.write(f"{file_name}/{password}\n")
    else:
        # create a new file and write the file_name and password
        file = open("access.txt", "w")
        file.write(f"{file_name}/{password}\n")

def list_file_names():
    files = os.listdir("uploads/")
    if len(files) == 0:
        data = "The server directory is empty"
    else:
        data = "\n".join(f for f in files)

    connectionSocket.send(data.encode())

def check_sum(file_name):
    block_size = 65536
    check_sum = hashlib.md5()
    binary_file = open(file_name, 'rb')
    for i in iter(lambda: binary_file.read(block_size), b''):
            check_sum.update(i)
    return check_sum.hexdigest()

# function to check if file name exists to avoid automatic renaming of files
def check_duplicates(file_name, count=1):
    file_names = os.listdir("uploads")
    new_file_name = file_name
    if new_file_name in file_names:
        temp = file_name.split(".")
        new_file_name = f"{temp[0]}_{count}.{temp[1]}"
        count += 1
        new_file_name = check_duplicates(new_file_name, count)
    return new_file_name

while True:
    #Server accepts the connection from the client
    connectionSocket, addr = serverSocket.accept()
    # Create a new thread to handle the client connection
    thread = threading.Thread(target=handle_client, args=(connectionSocket, addr))
    thread.start()

