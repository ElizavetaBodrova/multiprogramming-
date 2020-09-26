#pragma once
#include <queue>
#include <mutex>
#include<Windows.h>
#include<string>

class SafeThreadQueue
{
public:
	SafeThreadQueue();
	void push(int threadID,int value);
	int pop(int threadID);
	~SafeThreadQueue();

private:
	std::queue<int> m_queue;
	
	HANDLE hSemaphore;
};