package com.wl.old;

/*
 * 文件名：FFT.java
 * 基2-FFT时域抽取算法，java描述
 */

public class NewFFT{
	private double[] xConv;//对x[n]进行二进制倒序排列的结果
	
	public int init(int[] x)throws Exception{
		//int[] x;	存放采样值的数组
		int n,m,j;
		n = x.length;
		m = 0;
		j = 1;
		
		if(n < 1 || n >1024) throw new Exception("采样序列的个数小于1，或大于1024");//保证采样序列的个数介于1到1024之间
		for(int i=1;i<11;i++){
			j *= 2;
			m ++;
			if(j >= n) break;
		}

		xConv = new double[j];						//初始化xConv
		for(int i=0;i<n;i++)
			xConv[i] = x[i];
		
		if(j > n)									//xConv补零
			for(int i=n;i<j;i++)
				xConv[i] = 0;
		
		i2Sort(xConv,m);							//将xConv进行二进制倒序排序
		System.out.println("x[n]共有"+x.length+'('+m+"阶)"+"个采样值！"+"补零个数为："+(j-n)+'\n');

		int abc = myFFT(xConv,m);
		return abc;
	}

	/*
	* 方法名：i2Sort
	* 功能：二进制倒序排序，在进行基2-FFT蝶形算法前，可以用到此算法。
	* 参数：
	* doubl[] xConv2;需要排序的数组
	* int m;xConv2中元素所占2进制位宽
	* 返回值：void
	*/
	private void i2Sort(double[] xConv2, int m) {
		int[] index = new int[xConv2.length];		//index数组用于，倒序索引
		int[] bits = new int[xConv2.length];
		double[] temp = new double[xConv2.length];
		
		for(int i = 0;i < xConv2.length; i++)		//xConv2的原序映像
			temp[i] = xConv2[i];
		
		for(int i=0;i<index.length;i++){
			index[i] = i;							//第i个位置，倒序前的值为i
			for(int j=0;j<m;j++){
				bits[j] = index[i] - index[i]/2*2;	//提取index[i]的第j位二进制的值
				index[i] /= 2;
			}
			index[i] = 0;							//清零第i个位置的值
			for(int j = m,power = 1;j > 0;j --){
				index[i] += bits[j-1]*power;		//第i个位置，倒序后的位置
				power *= 2;
			}
			//System.out.println(index[i]);			//倒序效果预览
		}
		
		for(int i=0;i<xConv2.length;i++)			//倒序实现
			xConv2[i] = temp[index[i]];
		
	}

	/*
	* 方法名：myFFT
	* 功能：FFT算法
	* 参数：
	* double[] xConv2;	补零并进行二进制倒序后的x[n]
	* int m;	m = log2(n),n为采样个数
	* 返回值：void
	*/
	private int myFFT(double[] xConv2,int m) {
		int divBy;									//divBy等分
		double[] Xr,Xi,Wr,Wi;						//分别表示：FFT结果的实部和虚部、旋转因子的实部和虚部
		double[] tempXr,tempXi;						//蝶形结果暂存器
		int n = xConv2.length;
		double pi = Math.PI;
		divBy = 1;
		Xr = new double[n];
		Xi = new double[n];
		tempXr = new double[n];
		tempXi = new double[n];
		Wr = new double[n/2];
		Wi = new double[n/2];
		
		System.out.println("经过二进制倒序排列后的x[n]:");
		for(int i=0;i<n;i++){						//初始化Xr、Xi，之所以这样初始化，是为了方便下面的蝶形结果暂存
			Xr[i] = xConv2[i];
			Xi[i] = 0;
			System.out.println(String.format("%6.2f", xConv2[i]));
		}
		
		for(int i=0;i<m;i++){						//共需要进行m次蝶形计算
			divBy *= 2;
			for(int k=0;k<divBy/2;k++){				//旋转因子赋值
				Wr[k] = Math.cos(k*2*pi/divBy);
				Wi[k] = -Math.sin(k*2*pi/divBy);
			}

			for(int j=0;j<n;j++){					//蝶形结果暂存
				tempXr[j] = Xr[j];
				tempXi[j] = Xi[j];
			}
		
			for(int k=0;k<n/divBy;k++){					//蝶形运算：每一轮蝶形运算，都有n/2对的蝴蝶参与；n/2分为n/divBy组，每组divBy/2个。
				int wIndex = 0;							//旋转因子下标索引
				for(int j=k*divBy;j<k*divBy+divBy/2;j++){
					double X1 = tempXr[j+divBy/2]*Wr[wIndex] - tempXi[j+divBy/2]*Wi[wIndex];
					double X2 = tempXi[j+divBy/2]*Wr[wIndex] + tempXr[j+divBy/2]*Wi[wIndex];
					Xr[j] = tempXr[j] + X1;
					Xi[j] = tempXi[j] + X2;
					Xr[j+divBy/2] = tempXr[j] - X1;	//蝶形对两成员距离相差divBy/2
					Xi[j+divBy/2] = tempXi[j] - X2;
					wIndex ++;
//					System.out.println("j="+j);
				}
			}
		}
		
		System.out.println("FFT结果：");
		for(int i=0;i<n;i++)						//FFT结果显示
			System.out.println(String.format("%6.2f", Xr[i])+" + j"+String.format("%6.2f",Xi[i]));
		System.out.println("功率谱结果：");
		int[] tem = new int[32];
		for(int i=2;i<n/2;i++){						//FFT结果显示
			tem[i] = (int)((Xr[i])*(Xr[i])+ (Xi[i] * Xi[i]));
			System.out.println(i + " = " + ((Xr[i])*(Xr[i])+ (Xi[i] * Xi[i])));
		}
		int    max=tem[0];
		int flag=0;
        for(int i=1;i<tem.length;i++){
            if(max<tem[i]){
                max=tem[i];  //获得最大值
                flag = i;
            }
        }
        System.out.println("flag2=" + flag);
        return flag;
	}
	
	
//	public static void main(String[] args) throws Exception{
//		String abc100 = "129 135 135 131 125 128 131 135 136 136 137 129 128 129 130 134 133 137 141 140 127 130 132 136 134 134 131 133 140 133 133 134 132 141 136 134 135 131 132 130 133 138 136 137 135 141 131 132 130 133 133 134 136 138 129 130 130 135 137 134 134 147 135 135 130 130 130 131 134 141 141 127 135 134 134 132 134 138 138 127 130 131 127 132 137 139 127 127 133 133 128 130 144 134 127 133 132 135 132 131";
//		String abc10 = "1 2 3 4 5 6 7 8 9";
//		String abc = "127 131 125 134 134 130 137 137 140 126 129 129 135 128 129 135 135 131 125 128 131 135 136 136 137 129 128 129 130 134 133 137 141 140 127 130 132 136 134 134 131 133 140 133 133 134 132 141 136 134 135 131 132 130 133 138 136 137 135 141 131 132 130 133";
//		String[] tem = abc.split(" ");
//		int[] a = new int[tem.length];
//		for(int i=0;i<tem.length;i++){
//			a[i] = Integer.valueOf(tem[i]);
//		}
//		new NewFFT(a);
//	}
}
