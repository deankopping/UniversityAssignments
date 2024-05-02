import socket
import os
import hashlib

DOWNLOAD_DIR = "downloads/"

def send_file(clientSocket, file_name,password):
    clientSocket.send(f"UPLOAD/{file_name}".encode())
    file = open(file_name, "rb")
    file_size = os.path.getsize(file_name)
    check_sum(file_name)
    file_size= str(file_size)
    clientSocket.send(f"{password}/{file_size}/{check_sum(file_name)}".encode())
    data = file.read()
    clientSocket.sendall(data)
    file.close()
    done = clientSocket.recv(1024).decode()
    print(done)

def recieve_file(clientSocket, file_name):
    clientSocket.send(f"DOWNLOAD/{file_name}".encode())
    os.makedirs(DOWNLOAD_DIR, exist_ok=True)
    path = os.path.join(DOWNLOAD_DIR, file_name)
    msg = clientSocket.recv(1024).decode()
    characteristic = msg.split("/")
    if characteristic[0]=="OPEN":
        file_size = int(characteristic[1])
        file = open(path, "wb")
        file_bytes = 0
        while file_bytes < file_size:
            data = clientSocket.recv(1024)
            file_bytes += len(data)
            file.write(data)
            file.close()
        if (characteristic[2] == check_sum(file_name)):
            #print(f"{check_sum(file_name)}/{characteristic[2]}")
            print(f"{file_name} was uploaded successful")
            clientSocket.send("***********file was successfully uploaded***************".encode())
        else:
            #print(f"{check_sum(file_name)}/{file_data[2]}")
            print("file was unsuccessful")
            clientSocket.send("***********file failed uploaded***************".encode())
    elif characteristic[0]=="PROTECTED":
        print("PROTECTED")
        password = input("This file is protected. Please enter password:\n")
        clientSocket.send(password.encode())
        msg = clientSocket.recv(1024).decode()
        type = msg.split("/")
        #print(type[0], type[1])
        if(type[0]=="CORRECT"):
            #print(type[0])
            file_size = int(type[1])
            file = open(path, "wb")
            file_bytes = 0
            while file_bytes < file_size:
                data = clientSocket.recv(1024)
                file_bytes += len(data)
                file.write(data)
            file.close()
            if (type[2] == check_sum(file_name)):
                #print(f"{check_sum(file_name)}/{file_data[2]}")
                print("***********file was successfully downloaded***************")
                clientSocket.send("file was successfully downloaded".encode())
            else:
                #print(f"{check_sum(file_name)}/{file_data[2]}")
                print("***********file failed downloaded***************")
                clientSocket.send.encode("file failed downloaded".encode())
        elif type[0]=="INCORRECT":
            print("Password is incorrect. Unable to download file")

def List_files(clientSocket):
    clientSocket.send("LIST".encode())
    data = clientSocket.recv(1024).decode()
    print(data)

def check_sum(file_name):
    block_size = 65536
    check_sum = hashlib.md5()
    binary_file = open(file_name, 'rb')
    for i in iter(lambda: binary_file.read(block_size), b''):
            check_sum.update(i)
    return check_sum.hexdigest()

def main():
    # Address and port
    serverName = '127.0.0.1'
    serverPort = 12000
#option/filename/filesize/passkey/checksum
    # Create a TCP socket and connect to the server
    clientSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    clientSocket.connect((serverName, serverPort))
    try:
                menu = input("Enter the number\n""1.Upload\n""2.Download\n""3.View files\n""4.Exit\n")
                while menu != "4":
                    if menu == "1":
                        file_name = input("Enter a file name:\n")
                        secure = input("Do you want the file to be protected? (YES or NO)\n")
                        if secure== "YES":
                            password = input("Please enter a password:\n")
                            send_file(clientSocket,file_name,password)
                        elif secure == "NO":
                            send_file(clientSocket, file_name, "")
                        else:
                            print("please try again.")
                    elif menu == "2":
                        file_name = input("Enter a file name: ")
                        recieve_file(clientSocket, file_name)
                    elif menu == "3":
                        List_files(clientSocket)

                    else:
                        print("not a valid option, please try again ")
                    menu = input("Enter the number\n""1.Upload\n""2.Download\n""3.View files\n""4.Exit\n")

    finally:
            clientSocket.close()

if __name__=="__main__":
    main()
