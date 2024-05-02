.data
    .align 2
array:      .space  200
size:       .word   20
string:     .space  20000       
instruction: .asciiz "Enter n, followed by n lines of text:\n"
msg: .asciiz "The values are:\n"


    .text
    .globl main
main:
    # prompt user for array length
    li      $v0,4
    la      $a0,instruction
    syscall

    # read in array count
    li      $v0,5
    syscall
    addi    $s0,$v0,0           # $v0 contains the integer we read

    add     $t0,$zero,$zero     # index of array
    addi    $t1,$zero,1         # counter=1
    la      $s2,string          # load address of string storage area [NEW]

read_string:
    bgt     $t1,$s0,L1         


    # get the string
### la      $a0,string          # place to store string [OLD]
    move    $a0,$s2             # place to store string [NEW]
    li      $a1,20
    li      $v0,8
    syscall

    # store pointer to string into array
    sw      $a0,array($t0)

    addi    $t0,$t0,4          
    addi    $t1,$t1,1        
    addi    $s2,$s2,20        

    j       read_string


L1:
  #  add     $t0,$zero,$zero     # index of array
  addi $t0,$t0, -4
    addi    $t1,$zero,1         # counter = 1

    # output the title
    la      $a0,msg
    li      $v0,4
    syscall
  

printloop:
    bgt     $t1,$s0,exit        
    lw      $t2,array($t0)      # get pointer to string

    # output the string
    li      $v0,4
    move    $a0,$t2
    syscall
  

    addi    $t0,$t0,-4           # advance array index
    addi    $t1,$t1,1           # advance count
    j       printloop



exit:
    li      $v0,10
    syscall