package org.achartengine.util;

����   2 �
 % d e f e g h
  i
 j k
 j l m
  d	 $ n
 o p
 $ q
 o r
 o s
 t i u
  v e w
 j x>z��   
 $ y
 j z
 j {��      
 j |@$      
 j }@      @       
 o ~  � 
NULL_VALUE D 
ConstantValue������� FORMAT Ljava/text/NumberFormat; <init> ()V Code LineNumberTable LocalVariableTable this "Lorg/achartengine/util/MathHelper; minmax (Ljava/util/List;)[D value i I values Ljava/util/List; min max length LocalVariableTypeTable $Ljava/util/List<Ljava/lang/Double;>; 
StackMapTable � 	Signature ((Ljava/util/List<Ljava/lang/Double;>;)[D 	getLabels (DDI)Ljava/util/List; e Ljava/text/ParseException; z start end approxNumLabels labels labelParams [D 	numLabels N u )(DDI)Ljava/util/List<Ljava/lang/Double;>; 
computeLabels (DDI)[D tmp s switched Z xStep xStart xEnd roundUp (D)D val exponent rval <clinit> 
SourceFile MathHelper.java - . � � � � � java/lang/Double � � � ; � < � java/util/ArrayList + , � � � S T � � � � � java/text/ParseException � � � � � ] \ ] � ] � ] � ] � � � �  org/achartengine/util/MathHelper java/lang/Object java/util/List size ()I get (I)Ljava/lang/Object; doubleValue ()D java/lang/Math (DD)D java/text/NumberFormat setMaximumFractionDigits (I)V format (D)Ljava/lang/String; parse &(Ljava/lang/String;)Ljava/lang/Number; java/lang/Number valueOf (D)Ljava/lang/Double; add (Ljava/lang/Object;)Z abs ceil floor log10 pow getNumberInstance ()Ljava/text/NumberFormat; ! $ %     & '  (    )  + ,     - .  /   3     *� �    0   
    #  % 1        2 3   	 4 5  /    	   _*�  � ��*�  � � H'J*�  66� '*�  � � 9'� H)� J�����Y'RY)R�    0   .    . 	 / 
 1  2  3 % 4 / 5 ? 6 F 7 M 4 S 9 1   >  ?  6 '  ( + 7 8    _ 9 :    D ; '   B < '  % : = 8  >       _ 9 ?   @    
�   A  � * B    C 	 D E  /  s     {� Y� 	:� �� 
� &(� :11g1o�`66� =1�1kc9	� 
� 
	� 
� � 9	� :	� �  W�����  M ` c   0   :    G 	 H  I  K  L ! N 4 S > T M X ` [ c Y e \ r S x ^ 1   \ 	 e   F G  M % H ' 	 7 A 7 8    { I '     { J '    { K 8  	 r L :  ! Z M N  4 G O 8  >     	 r L ?  @   * �  A� % P� +  A P  Q�  B    R 
 S T  /  l     �&(g�  �� �Y&RY&RYR�&9(96	�� 6	9
9
9g� �o� 9


o� k9

o� k9	� �YRYRY 
kR��YRYRY
R�    0   B    k 
 l  n   o # p & q . r 1 s 5 t 9 u = w N y [ z h { m | � ~ 1   f 
 5  U ' 
   � I '     � J '    � K 8    w V '  # t F '  & q W X 	 N I Y ' 
 [ < Z '  h / [ '  @    � � F 
 \ ]  /   �     J&� � �=& t�� kJ) �� 
 J� ) !�� 
 J� 
)��  !J) �� kJ)�    0   * 
   � 	 �  �  � $ � , � 3 � 9 � = � H � 1        J ^ '   	 A _ 8   5 ` '  @   	 � $	  a .  /         � #� 
�    0       !  b    c