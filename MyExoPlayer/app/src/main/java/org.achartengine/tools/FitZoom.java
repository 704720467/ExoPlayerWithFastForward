package org.achartengine.tools;

����   2 �
  ?	  @ A
  B	  C
 D E
 D F
 D G
 D H
 D I
 J K���������������
 L M
 L N
 O P
 L Q
 O R
 L S
 L T
 O U@D       V
  W
 X Y
 X Z [ \ <init> )(Lorg/achartengine/chart/AbstractChart;)V Code LineNumberTable LocalVariableTable this  Lorg/achartengine/tools/FitZoom; chart &Lorg/achartengine/chart/AbstractChart; apply ()V i I j marginX D marginY series "[Lorg/achartengine/model/XYSeries; range [D length scales renderer +Lorg/achartengine/renderer/DefaultRenderer; 
StackMapTable [ 2 4 
SourceFile FitZoom.java   ! ] ( org/achartengine/chart/XYChart ^ _ ` a b c d e f e g h i j k l m n o p d q r s t u v r w u x r y r z { !org/achartengine/chart/RoundChart | } ~  � � � org/achartengine/tools/FitZoom #org/achartengine/tools/AbstractTool mChart 
getDataset 2()Lorg/achartengine/model/XYMultipleSeriesDataset; 	mRenderer 4Lorg/achartengine/renderer/XYMultipleSeriesRenderer; 2org/achartengine/renderer/XYMultipleSeriesRenderer getScalesCount ()I isInitialRangeSet ()Z (I)Z getInitialRange (I)[D setRange ([DI)V .org/achartengine/model/XYMultipleSeriesDataset 	getSeries $()[Lorg/achartengine/model/XYSeries; org/achartengine/model/XYSeries getScaleNumber getMinX ()D java/lang/Math min (DD)D getMaxX max getMinY getMaxY abs (D)D getRenderer -()Lorg/achartengine/renderer/DefaultRenderer; )org/achartengine/renderer/DefaultRenderer getOriginalScale ()F setScale (F)V !           !  "   >     *+� �    #   
       ! $        % &      ' (   ) *  "  �  
  \*� � �A*� � � � �*� � <*� � � .=� $*� � � *� *� � 	� 
���ݧ �*� � � � MN,�6� �6� ��Y RY RY RY RN6� U,2� � C--1,2� � R--1,2� � R--1,2� � R--1,2� � R����-1-1g�  o9-1-1g�  o9*� �Y-1gRY-1cRY-1gRY-1cR� 
���/� *� � � L++� � �    #   z    ' 
 (  )  +   , * - 1 . < / L - U 3 c 4 e 5 i 6 n 7 w 8 � : � ; � < � = � > � ? � : � B � C D? 7E IH JS K[ M $   p  , & + ,  � Y - ,  � @ . /  0 0 /  q � + ,  c � 1 2  e � 3 4  i � 5 ,   % 6 , S  7 8   \ % &   9   6 � � �   : ; <  � $� R� � U  :    =    >