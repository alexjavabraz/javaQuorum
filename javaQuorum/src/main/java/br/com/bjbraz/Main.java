package br.com.bjbraz;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;
import org.web3j.quorum.methods.request.PrivateTransaction;
import org.web3j.quorum.methods.response.QuorumNodeInfo;

public class Main {
	
	private static final String URL = "http://quorum.astar.tech:22001";
	
	public static void main(String[] args) {
		try{
			Quorum quorum = Quorum.build(new HttpService(URL));
			QuorumNodeInfo quorumNodeInfo = quorum.quorumNodeInfo().sendAsync().get();
			String voteAccount = quorumNodeInfo.getNodeInfo().getVoteAccount();
			String mkr = quorumNodeInfo.getNodeInfo().getBlockMakerAccount();
			
			System.out.println("Maker Account " + mkr);
			System.out.println("Vote Account " + voteAccount);
			
			Web3j web3 = Web3j.build(new HttpService(URL));
			Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
			String clientVersion = web3ClientVersion.getWeb3ClientVersion();

			System.out.println(clientVersion);
			
			listarContas(web3);
			
			printaDadosDaConta("0xca843569e3427144cead5e4d5999a3d0ccf92b8e");
			
			printaDadosDaConta("0x0fbdc686b912d7722dc86510934589e0aaf3b55a");
			
			printDadosDosBlocos(web3);
			
			sendTransaction(quorum);
			
			getTransactionDetails(web3);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void getTransactionDetails(Web3j web3) {
		/*
		 * 0x8c8cdb8dd47c3a9733e07ffec0ebf269f00ba6cdd012295448d1b341a1513ae2
			0x79e71c2568d21482fc79befeaab0d9a00a28349c934bf8fd0db72645a856af43
			0x7a0480bfde56e23ca3b7dec63b619523a5916e27f85da4980b56b5cf288e4e3b
			0xfdb6c21131cb6e3e8508731d236df2be6c77a88221aac0f98dab6bcf7de588e3
			
			
			1000447835000000000000000000
			1000447850000000000000000000
			1000447865000000000000000000
			1000447930000000000000000000
			1000451090000000000000000000
			*/
		 
	}

	/**
	 * 
	 * @param quorum
	 */
	private static void sendTransaction(Quorum quorum) {
		BigInteger gas = new BigInteger("21000");
		
		try{
			/**
			 * 
			 */
			EthSendTransaction sendTransaction = quorum.ethSendTransaction(
					new PrivateTransaction(
								"0x0fbdc686b912d7722dc86510934589e0aaf3b55a", 
								BigInteger.ONE, 
								gas, 
								"0xca843569e3427144cead5e4d5999a3d0ccf92b8e", 
								BigInteger.ONE, 
								"DATA", 
								Arrays.asList("private1", "private2")
							)).send();
			if(sendTransaction.hasError()){
				System.out.println(sendTransaction.getError().getMessage());
			}
			System.out.println(sendTransaction.getResult());
			
			System.out.println("Transaction HASH : " + sendTransaction.getTransactionHash());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static void printDadosDosBlocos(Web3j web3) {
		try{
			EthBlockNumber blockcknumber = web3.ethBlockNumber().send();
			System.out.println(blockcknumber.getBlockNumber());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param web3
	 */
	private static void listarContas(Web3j web3) {
		try{
			Request<?, EthAccounts> requestContas = web3.ethAccounts();
	
			List<String> contas = requestContas.send().getAccounts();
			
			for(String conta : contas){
				System.out.println(conta);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}

	/**
	 * 
	 * @param mkr
	 */
	private static void printaDadosDaConta(String account) {
		try{
			Web3j web3 = Web3j.build(new HttpService(URL));
			EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(
					account, DefaultBlockParameterName.LATEST).sendAsync().get();
			
			Request<?, EthGetBalance>  balanceRequest = web3.ethGetBalance(account, DefaultBlockParameterName.LATEST);
			
			EthGetBalance balance = balanceRequest.send();
			
			System.out.println("Balance: " + balance.getBalance());
			
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();
			
			System.out.println("Usando a conta [" + account+"]");
			System.out.println("Nonce: " + nonce);
			
		}catch(Exception e){
			System.out.println(e);
		}
		
	}

}
