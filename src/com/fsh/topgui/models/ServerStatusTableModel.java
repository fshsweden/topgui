package com.fsh.topgui.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.ev112.codeblack.atc.connections.CONNECTION_STATUS;
import com.ev112.codeblack.atc.connections.PriceCollectorConnection;
import com.ev112.codeblack.atc.connections.PriceCollectorConnection;
import com.ev112.codeblack.atc.connections.PriceConnectionCallback;
import com.ev112.codeblack.atc.connections.RiskConnectionCallback;
import com.ev112.codeblack.atc.connections.RiskControllerConnection;
import com.ev112.codeblack.atc.connections.RiskControllerConnection;
import com.ev112.codeblack.atc.connections.ServerConnection;
import com.ev112.codeblack.atc.connections.ServerConnectionCallback;
import com.ev112.codeblack.atc.connections.StrategyConnectionCallback;
import com.ev112.codeblack.atc.connections.StrategyServerConnection;
import com.ev112.codeblack.atc.connections.StrategyServerConnection;
import com.ev112.codeblack.common.configuration.Configuration;
import com.ev112.codeblack.common.generated.messages.PLStrategy;
import com.ev112.codeblack.common.generated.messages.RiskController_PositionStatusBdx;
import com.ev112.codeblack.common.generated.messages.StatusEvent;
import com.ev112.codeblack.common.generated.messages.StrategyServer_OwnOrder;
import com.ev112.codeblack.common.generated.messages.StrategyServer_OwnTrade;

public class ServerStatusTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -8132531768705472386L;
	private Map<Integer,ServerStatus> serverStatuses = new HashMap<Integer,ServerStatus>();
	private StrategyServerConnection strategyServerConnection;
	private PriceCollectorConnection priceCollectorConnection;
	private RiskControllerConnection riskControllerConnection;
	private Configuration config;
	
	/* Data */
	public class ServerStatus {
		private String name;
		private String status;
		private Boolean connected = false;
		private Integer inmsgCount = 0;
		private Integer outmsgCount = 0;
		private ServerConnection pConnection = null;
		
		public ServerStatus(final String name, final String status) {
			this.name = name;
			this.status = status;
		}
		
		String getColValue(final int col) {
			switch (col) {
				case 0:return name; 
				case 1:return status;
				default:
				case 2:return connected ? "Connected" : "";
				case 3: {
					if (getServerConnection() != null) {
						return String.format("%d", getServerConnection().getNrInMsgs());
					}
				}
				break;
				case 4: {
					if (getServerConnection() != null) {
						return String.format("%d", getServerConnection().getNrOutMsgs());
					}
				}
				break;
			}
			
			return "";
		}
		
		public void setConnected(ServerConnection pConnection, Boolean b) {
			this.pConnection = pConnection;
			connected = b;
			fireTableDataChanged();
		}
		
		public ServerConnection getServerConnection() {
			return pConnection;
		}
		
		public Boolean isConnected() {
			return connected;
		}

		public Integer getOutmsgCount() {
			return outmsgCount;
		}

		public void setOutmsgCount(Integer outmsgCount) {
			this.outmsgCount = outmsgCount;
		}

		public Integer getInmsgCount() {
			return inmsgCount;
		}

		public void setInmsgCount(Integer inmsgCount) {
			this.inmsgCount = inmsgCount;
		}
		
		
	}
	
	/*
	 * 
	 */
	public ServerStatusTableModel() {
		serverStatuses.put(0, new ServerStatus("PriceCollector",""));
		serverStatuses.put(1, new ServerStatus("StrategyServer",""));
		serverStatuses.put(2, new ServerStatus("RiskManager",""));

		config = new Configuration("TESTREMOTE");
		
		ServerConnectionCallback serverStrategySCC = new ServerConnectionCallback() {
			@Override
			public void serverStatisticsUpdate() {
				System.out.println("SS serverStatisticsUpdate()");
				fireTableDataChanged();			
			}
			
			@Override
			public void connectionStatusChangeCallback(ServerConnection pConnection, CONNECTION_STATUS pStatus) {
				ServerStatus ss = serverStatuses.get(1);
				ss.setConnected(pConnection, pStatus == CONNECTION_STATUS.CONNECTED);
			}
		};
		
		StrategyConnectionCallback strategySCC = new StrategyConnectionCallback() {
			@Override
			public void strategyUnloaded(String strategy, Integer status) {
			}
			
			@Override
			public void strategyLoaded(String strategy, Integer status) {
			}
			
			@Override
			public void strategyListUpdated(List<PLStrategy> pStrategies) {
			}
			
			@Override
			public void strategyListReply(List<PLStrategy> pStrategies) {
			}
			
			@Override
			public void setMatcher(String pMatcher) {
			}
			
			@Override
			public void addTrade(StrategyServer_OwnTrade pTrade, boolean pBroadcast) {
			}
			
			@Override
			public void addOrder(StrategyServer_OwnOrder pOrder, boolean pBroadcast) {
			}
			
			@Override
			public void addEvent(List<StatusEvent> pEvents) {
			}
		};
		
		strategyServerConnection = new StrategyServerConnection(serverStrategySCC, strategySCC, config, "StrategyServer", true);
		
		ServerConnectionCallback serverPriceSCC = new ServerConnectionCallback() {  // BUG: missing ServerConnection handle!
			@Override
			public void serverStatisticsUpdate() {
				System.out.println("PC serverStatisticsUpdate()");
				fireTableDataChanged();
			}
			@Override
			public void connectionStatusChangeCallback(ServerConnection pConnection, CONNECTION_STATUS pStatus) {
				ServerStatus ss = serverStatuses.get(0);
				ss.setConnected(pConnection, pStatus == CONNECTION_STATUS.CONNECTED);
			}
		};
		
		PriceConnectionCallback priceSCC = new PriceConnectionCallback() {
			@Override
			public void addEvent(List<StatusEvent> pEvents) {
			}
		};
		priceCollectorConnection = new PriceCollectorConnection(serverPriceSCC, priceSCC, config, "PriceCollector", true);
		
		ServerConnectionCallback serverRiskSCC = new ServerConnectionCallback() {
			@Override
			public void serverStatisticsUpdate() {
				System.out.println("RM serverStatisticsUpdate()");
				fireTableDataChanged();
			}
			@Override
			public void connectionStatusChangeCallback(ServerConnection pConnection, CONNECTION_STATUS pStatus) {
				ServerStatus ss = serverStatuses.get(2);
				ss.setConnected(pConnection, pStatus == CONNECTION_STATUS.CONNECTED);
			}
		};
		
		RiskConnectionCallback riskSCC = new RiskConnectionCallback() {
			@Override
			public void updateRiskData(RiskController_PositionStatusBdx pRiskUpdate) {
				// TODO implement this
			}
			@Override
			public void addEvent(List<StatusEvent> pEvents) {
				// TODO implement this
			}
		};
		riskControllerConnection = new RiskControllerConnection(serverRiskSCC, riskSCC, config, "RiskController", true);
		
		
		// manual start.....
		priceCollectorConnection.start();
		strategyServerConnection.start();
		riskControllerConnection.start();
	}
	
	// fireTableDataChanged();   whenever data changes!!
	
	@Override
	public int getRowCount() {
		return serverStatuses.size();
	}

	@Override
	public int getColumnCount() {
		// TODO implement this
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ServerStatus r = serverStatuses.get(rowIndex);
		if (r == null) {
			return null;
		}
		return r.getColValue(columnIndex);
		
	}

}
