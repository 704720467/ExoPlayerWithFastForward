package org.achartengine;

����   2 �
 < { |
  {	 ; }	 ; ~
  � �
  �	 ; � �
 
 �
 � � �
 
 �	 ; �
 � � �
  �	 ; �
 � �	 ; �	 ; �
 � �
 � �
 � �	 ; �	 ; �
 � �?�      
 ; �@
�
=p��
 
 �
  �
  �	  �
  �@@  
  �
  �
  ���  
 � �?fff
 � �?���
 � �?�������?񙙙���
  �
  �
  �
  �
 
 �
 
 � � � � 	mRenderer +Lorg/achartengine/renderer/DefaultRenderer; oldX F oldY oldX2 oldY2 zoomR Landroid/graphics/RectF; mPan Lorg/achartengine/tools/Pan; 
mPinchZoom Lorg/achartengine/tools/Zoom; 
graphicalView  Lorg/achartengine/GraphicalView; <init> I(Lorg/achartengine/GraphicalView;Lorg/achartengine/chart/AbstractChart;)V Code LineNumberTable LocalVariableTable this Lorg/achartengine/TouchHandler; view chart &Lorg/achartengine/chart/AbstractChart; 
StackMapTable � � � handleTouch (Landroid/view/MotionEvent;)Z newX2 newY2 	newDeltaX 	newDeltaY 	oldDeltaX 	oldDeltaY zoomRate tan1 tan2 newX newY event Landroid/view/MotionEvent; action I � 	applyZoom (FI)V axis addZoomListener ((Lorg/achartengine/tools/ZoomListener;)V listener %Lorg/achartengine/tools/ZoomListener; removeZoomListener addPanListener '(Lorg/achartengine/tools/PanListener;)V $Lorg/achartengine/tools/PanListener; removePanListener 
SourceFile TouchHandler.java M � android/graphics/RectF E F K L � � � org/achartengine/chart/XYChart � � > ? !org/achartengine/chart/RoundChart � � � � � org/achartengine/tools/Pan M � G H � � org/achartengine/tools/Zoom M � I J � � � @ A B A � � � � � � C A D A � � � m n � � � � � � � A � � � � � � � � � � � � � � � � � � p q t q u v x v org/achartengine/TouchHandler java/lang/Object org/achartengine/ITouchHandler org/achartengine/GraphicalView $org/achartengine/chart/AbstractChart android/view/MotionEvent ()V getZoomRectangle ()Landroid/graphics/RectF; getRenderer 6()Lorg/achartengine/renderer/XYMultipleSeriesRenderer; -()Lorg/achartengine/renderer/DefaultRenderer; )org/achartengine/renderer/DefaultRenderer isPanEnabled ()Z )(Lorg/achartengine/chart/AbstractChart;)V 
isZoomEnabled +(Lorg/achartengine/chart/AbstractChart;ZF)V 	getAction ()I getX (I)F getY getPointerCount java/lang/Math abs (F)F apply (FFFF)V repaint contains (FF)Z left width ()F zoomIn zoomOut 	zoomReset isClickEnabled max (FF)F min setZoomRate (F)V (I)V ! ; <  = 	  > ?    @ A    B A    C A    D A    E F    G H    I J    K L     M N  O   �     n*� *� Y� � *+� **� � � ,� � *,� � � 	� *,� 
� � 	*� 	� � *� 
Y,� � *� 	� � *� Y,� � �    P   2    ;  -  <  =  > & ? 4 A ? C I D U F _ G m I Q        n R S     n T L    n U V  W    � 4  X Y Z  
  [ \  O      P+� =*� 	�z�u*� �� *� �� +� F+� 8+� �*� �� *� ��*� 	� � �+� 8+� 8%f� 8f� 8*� *� f� 8	*� *� f� 8
8*� f� %*� f� n8*� f� *� f� n8
� �� 
� �� 	n8*� � W�  �� 
�  �� 
n8*� � 2%*� f� *� f� �� 
	n8� 

n8*� *� *� � )*� 	� � *� *� *� %� "*� *� *%� *� *� � #�� �*+� � *+� � *� 	� �*� 	� � �*� *� *� � $� �*� *� � %*� � &'nb�� 
*� � (� 0*� *� � %*� � &
j'nb�� 
*� � )� 
*� � *�� 	� )*� *� *� *� � *+� *+� *� 	� ,� � �    P   � 6   Q  R  S # T ) U 0 V T W [ X b Y k Z u [ � \ � ] � _ � ` � a � d � e � f � i j m" n, p3 r: t@ uF vS we xj yo {t |z }� ~� �� �� �� �� �� �� �� � �
 � � � �$ �) �. �4 �: �@ � Q   �  [ � ] A  b � ^ A  k � _ A  u � ` A  � � a A 	 � � b A 
 � � c A  � � d A  � � e A 
 )Z f A  0S g A   P R S    P h i  K j k  W   G � #� &� �  X l  $ �   X l  %� � \%
%
@  m n  O   �     8#-� .D#/� 0D*� � %#� 1�� #� 3�� *� #� 5*� � 6�    P       �  �  � ' � / � 7 � Q        8 R S     8 c A    8 o k  W    7  p q  O   U     *� � *� +� 7�    P       �  �  � Q        R S      r s  W      t q  O   U     *� � *� +� 8�    P       �  �  � Q        R S      r s  W      u v  O   U     *� � *� +� 9�    P       �  �  � Q        R S      r w  W      x v  O   U     *� � *� +� :�    P       �  �  � Q        R S      r w  W      y    z