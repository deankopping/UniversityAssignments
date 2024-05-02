import os
import socket
import hashlib


DOWNLOAD_DIR = "downloads/"

def FileUpload(Socket, FileInfo):
    
    try:
        #Seperate user input into file name and file password
        FileInfoList = FileInfo.split("/")
        FileName = FileInfoList[0]
        FilePassword = FileInfoList[1]
        
        Socket.send(f"UPLOAD/{FileName}".encode())
        file = open(FileName, "rb")
        file_size = os.path.getsize(FileName)
        
        ValidateFile(FileName)
        file_size= str(file_size)
        Socket.send(f"{FilePassword}/{file_size}/{ValidateFile(FileName)}".encode())
        FileData = file.read()
        Socket.sendall(FileData)
        file.close()
    
        #Confirmation of successful upload to user
        Success = Socket.recv(1024).decode()
        print(Success)
    except:
        print("File does not exist")
    
    
def FileDownload(Socket, FileName):
    
    Socket.send(f"DOWNLOAD/{FileName}".encode())
    os.makedirs(DOWNLOAD_DIR, exist_ok=True)
    path = os.path.join(DOWNLOAD_DIR, FileName)
    
    FileInfo = (Socket.recv(1024).decode()).split("/")
    
    if FileInfo[0]=="OPEN":
        FileSize = int(FileInfo[1])
        file = open(path, "wb")
        FileBytes = 0
        
        #writes data to file
        while FileBytes < FileSize:
            data = Socket.recv(1024)
            FileBytes += len(data)
            file.write(data)
            file.close()
            
        if (FileInfo[2] == ValidateFile(FileName)):
            #file valid
            print(f"{FileName} has been downloaded")
            Socket.send("File download successful".encode())
        else:
            #invalid file
            print("file was invalid")
            Socket.send("File download failed".encode())
            
    elif FileInfo[0]=="PROTECTED":
        
        password = input("This file is protected. Please enter password:\n")
        Socket.send(password.encode())
        msg = Socket.recv(1024).decode()
        type = msg.split("/")
        
        if(type[0]=="CORRECT"):
            
            FileSize = int(type[1])
            file = open(path, "wb")
            FileBytes = 0
            
            #writes all the data to the file
            while FileBytes < FileSize:
                data = Socket.recv(1024)
                FileBytes += len(data)
                file.write(data)
            file.close()
            
            if (type[2] == ValidateFile(FileName)):
                #file valid
                print("File download successful")
                Socket.send("file was successfully downloaded".encode())
            else:
                #file invalid
                print("File download failed")
                Socket.send.encode("File download failed".encode())
                
        elif type[0]=="INCORRECT":
            print("Password is incorrect. Unable to download file")
    
def ValidateFile(FileName):
    #File validation 
    BlockSize = 65536
    ValidateFile = hashlib.md5()
    binary_file = open(FileName, 'rb')
    
    for i in iter(lambda: binary_file.read(BlockSize), b''):
            ValidateFile.update(i)
    return ValidateFile.hexdigest()

def Viewfiles(Socket):
    
    Socket.send("LIST".encode())
    
    #recieves string containing file names from server
    FileList = Socket.recv(1024).decode()
    print(FileList)
    
def main():
    
    # Address and port
    Host = '127.0.0.1'
    Port = 12000
    
    #gives option to user to change the server IP and port number
    print("The recommended Server IP adress is '127.0.0.1' and port number is 12000.\n")
    Change = input("if you would like to change these settings type 'YES', if not, press enter.\n")

    if Change =="YES":
        try:
            settings = input("Input your Server IP adress and Port number seperated by '/'.\n")
            settings = settings.split("/")

            #re assigns the host and port variables to the values given by user
            Host = settings[0]
            Port = settings[1]
    
            # TCP socket
            Socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            # Connection to server
            Socket.connect((Host, Port))
     
        except:
            print("Invalid IP adress or port number, connnection with server not made\n")
            #exits program if IP and port not valid
            exit()
     # TCP socket
    Socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # Connection to server
    Socket.connect((Host, Port))
        
    try:        
                #repeats until user selects to exit
                Action = input("Please select the action you would like to perform\n""Upload\n""Download\n""View files\n""Exit\n")
                while Action != "Exit":
                    if Action == "Upload":
                        
                        print("File upload: Enter the file name followed by '/'\n")
                        FileInfo = input("If you would like this file to be protected, enter the password after '/'\n") 
                        FileUpload(Socket,FileInfo)
                       
                    elif Action == "Download":
                        print("File Download\n")
                        FileName = input("Enter the name of the file you would like to download\n")
                        FileDownload(Socket, FileName)
                        
                    elif Action == "View files":
                        Viewfiles(Socket)
                        print("\n")

                    else:
                        print("invalid action, please try again ")
                        
                    #prompts user for next action
                    Action = input("Enter the action\n""Upload\n""Download\n""View files\n""Exit\n")

    finally:
            Socket.close()
if __name__=="__main__":
    main()