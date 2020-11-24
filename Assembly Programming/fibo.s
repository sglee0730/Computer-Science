.data
	fibs: .word   0 : 20	       
	size: .word              
	prompt: .asciiz "input X for fibonacci(x): "	
	prompt2: .asciiz "input X for fibonacci(x): "
	result: .asciiz  "result: "
	newline: .asciiz "\n"
	space: .asciiz " + "
	equal: .asciiz " = "
      
.text

start:
	la   $a0, prompt        
	li   $v0, 4           
	syscall              
	li   $v0, 5           
	syscall               

check:
	li   $s4,-1
	beq  $s4, $v0, end

init:
	la   $t0, fibs       
	la   $t5, size
	addi $v0, $v0, 1
	sw   $v0, 0($t5)
	lw   $t5, 0($t5)     
	li   $t2, 1
	li   $t3, 0
	li   $t6, 1
	sw   $t3, 0($t0)      
	sw   $t2, 4($t0)      
	addi $t1, $t5, -2     
	
loop: 
	lw   $t3, 0($t0)       
	lw   $t4, 4($t0)      
	add  $t2, $t3, $t4    
	sw   $t2, 8($t0)     
	addi $t0, $t0, 4     
	addi $t1, $t1, -1     
	bgtz $t1, loop       
	la   $a0, fibs        
	add  $a1, $zero, $t5  
	jal  print           
	li   $v0, 10         
	syscall               
		
print:
	add  $t0, $zero, $a0  
	add  $t1, $zero, $a1  
	la   $a0, result        
	li   $v0, 4           
	syscall               
	
out:  
	lw   $a0, 0($t0)      
	li   $v0, 1           
	syscall
	addi $t0, $t0, 4      
	addi $t1, $t1, -1     
	bgtz $t1, add         
	la   $a0, newline        
	li   $v0, 4           
	syscall
	la   $a0, prompt2        
	li   $v0, 4           
	syscall
	j    start

eq:
	la   $a0, equal        
	li   $v0, 4           
	syscall
	j    out

add:
	beq  $t1, $t6, eq
	la   $a0, space        
	li   $v0, 4           
	syscall
	j    out
	
end:
	jr   $ra

