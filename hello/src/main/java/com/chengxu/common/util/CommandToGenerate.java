package com.chengxu.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import java.util.List;

/**
 * @ClassName CommandToGenerate
 * @Description 迁移命令生成
 * @Author ruanchanglong
 * @Date 2020/4/9 22:07
 * @Version 1.0
 **/
public class CommandToGenerate {

	public static void main(String[] args) {
		//参数 systemVm系统盘名称GZW-APP-Srv27-10.254.13.27cl.ova
		String systemVm ="JTT-APP-Svr22-10.200.18.176-clone-disk1.vmdk";
		//参数 dataVm数据盘名称1
		String dataVm1 ="JTT-APP-Svr22-10.200.18.176-clone-disk2.vmdk";
		//参数 dataVm数据盘名称2
		String dataVm2 ="";
		//虚机名称
		String vmName="JTT-APP-Svr22-10.200.18.176-clone-disk";
		//todo IP 注意变化   158-161
		String ip="10.200.18.176";
		//32_65536_100_0_0     32/64  100
		//16_32768_100_0_0     16/32  100
		//4_90112_100_0_0      4/8    100
		String flavor="4_90112_100_0_0";
		//可启动镜像目录 todo 一般固定上不修改
		String moveVm ="/var/lib/libvirt/images/ruanchanglong/";
		// 以下 todo 斟酌修改
		//sh脚本名称
		String sh="11.sh";
		//磁盘数量
		String vmNum="2";
		//系统
		String system="linux";
		//磁盘格式
		String diskFormat="qcow2";
		//网络vlanId
		String netId="3d592c7e-f9db-4f46-872b-59041c139039";
		String az="hlw_cluster_02";
		String VOLUME_POOL="volumes";
		//上传openstack路径
		String upVm ="scp -i /home/user3/user1 user3@kvm03:/var/lib/libvirt/images/ruanchanglong/upVm .";
		String updataVm ="scp -i /home/user3/user1 user3@kvm03:/var/lib/libvirt/images/ruanchanglong/upVm .";
		//todo 以上 是参数输入
		//1windows工作机文件解压上传到kvm工作机(手动工具上传)
		//2查看镜像大小
		String info ="qemu-img info ";
		String lookAtMirror = info + systemVm;
		//3转换镜像
		String convert="qemu-img convert -p -f vmdk -O qcow2 ";


		//4移动镜像至可启动文件下
		String systemQ2Vm = systemVm.replaceAll("vmdk", "qcow2");
		String convertImage = convert + systemVm+" "+systemQ2Vm;
		String mvCommand = "mv " + systemQ2Vm + " " + moveVm;
		//5启动镜像
		String startVm="bash vm-define.sh ruan1 $PWD/VM $PWD/test-data.qcow2 $PWD/virtio-win-0.1.171.iso";
		String openVm = startVm.replace("VM", systemQ2Vm);
		//转换镜像
		String convertData1 = convert + dataVm1+" "+dataVm1.replaceAll("vmdk", "qcow2");
		String convertData2 = convert + dataVm2+" "+dataVm2.replaceAll("vmdk", "qcow2");
		List<String> row5 = CollUtil.newArrayList("启动前确认虚机运行情况", "");
		List<String> row6 = CollUtil.newArrayList("查看运行虚机", "virsh list");
		List<String> row12 = CollUtil.newArrayList("关闭运行虚机","virsh destroy test virsh undefine test ");
		List<String> row7 = CollUtil.newArrayList("启动镜像", openVm);
		List<String> row8 = CollUtil.newArrayList("数据盘1转换镜像", convertData1);
		List<String> row9 = CollUtil.newArrayList("数据盘2转换镜像", convertData2);
		//6上传openstack todo
		String upOpenStack = upVm.replaceAll("upVm", systemQ2Vm);
		String upData1OpenStack = updataVm.replaceAll("upVm", dataVm1.replaceAll("vmdk", "qcow2"));
		String upData2OpenStack = updataVm.replaceAll("upVm", dataVm2.replaceAll("vmdk", "qcow2"));
		List<String> row10 = CollUtil.newArrayList("注意开始上传到openstack");
		List<String> row13 = CollUtil.newArrayList("disk1上传openstack", upOpenStack);
		List<String> row14 = CollUtil.newArrayList("disk2上传openstack", upData1OpenStack);
		List<String> row15= CollUtil.newArrayList("disk3上传openstack", upData2OpenStack);
		System.out.println("注意开始上传到openstack");
		//执行openstack创建虚机挂盘网络脚本
		String bash="bash";
		String openstackScript =bash+" "+sh+" "+vmName+" "+vmNum+" "+system+" "+diskFormat+" "+netId+" "+ip+" "+flavor
				+" "+az+" "+VOLUME_POOL;

		List<String> row11 = CollUtil.newArrayList("openstack执行脚本",openstackScript);
		List<String> row1 = CollUtil.newArrayList("查看镜像大小", lookAtMirror);
		List<String> row2 = CollUtil.newArrayList("系统盘转换镜像", convertImage);
		List<String> row3 = CollUtil.newArrayList("移动至可启动文件下", mvCommand);
		List<String> row4 = CollUtil.newArrayList("数据盘操作", "注意数据盘");

		List<List<String>> rows = CollUtil.newArrayList(row1, row2, row3,row5,row6,row12,row7,row10,row13,row4,row8,row9,row14,row15,row11);
		createExcel(vmName, row1, rows);
	}

	private static void createExcel(String vmName, List<String> row1, List<List<String>> rows) {
		String i= IdUtil.randomUUID();
		ExcelWriter writer = ExcelUtil.getWriter("d:/执行脚本命令/"+vmName+i+".xlsx");
		writer.passCurrentRow();
		//合并单元格后的标题行，使用默认标题样式
		writer.merge(row1.size() - 1, "执行脚本命令");
		//一次性写出内容，强制输出标题
		writer.write(rows, true);
		writer.setColumnWidth(0,25);
		writer.setColumnWidth(1,125);
		//关闭writer，释放内存
		writer.close();
	}
}
