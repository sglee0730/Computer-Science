.data
        A:  .word 4
        B:  .word 2
        C:  .word 10
        D:  .word 2
        value:  .word 0
        msg:    .asciiz "value: "

.text
.globl main

    main:
        lw		$s0, A
        lw		$s1, B
        lw		$s2, C
        lw		$s3, D		

        add		$t0, $s0, $s1	
        sub		$t1, $s2, $s3

        mult	$t0, $t1		
        mflo	$t0		

        sw		$t0, value		

        li		$v0,4
        la      $a0, msg
        syscall
        
        li		$v0, 1
        lw		$a0, value
        syscall

        li		$v0,10
        syscall






