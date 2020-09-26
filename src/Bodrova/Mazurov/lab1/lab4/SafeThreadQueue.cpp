#include "SafeThreadQueue.h"
#include <iostream>

SafeThreadQueue::SafeThreadQueue() {
	hSemaphore = CreateSemaphore(
		NULL,	// нет атрибута
		0,	// начальное состояние
		1,	// максимальное состояние
		NULL	// без имени
		);
}
void SafeThreadQueue::push(int threadID, int value)
{
	//hSemaphore = CreateSemaphore(
	//	NULL,	// нет атрибута
	//	1,	// начальное состояние
	//	1,	// максимальное состояние
	//	NULL	// без имени
	//	);

	WaitForSingleObject(
		hSemaphore,   // handle to semaphore
		1L);
	m_queue.push(value);
	std::cout << "w" << threadID << " " << value << std::endl;
	ReleaseSemaphore(hSemaphore,1, NULL);
}

int SafeThreadQueue::pop(int threadID)
{
	//hSemaphore = CreateSemaphore(
	//	NULL,	// нет атрибута
	//	1,	// начальное состояние
	//	1,	// максимальное состояние
	//	NULL	// без имени
	//	);

	WaitForSingleObject(
		hSemaphore,   // handle to semaphore
		1L);
	if (!m_queue.empty()) {
		int val = m_queue.front();
		m_queue.pop();
		std::cout << "r" << threadID << " " << val << std::endl;
		ReleaseSemaphore(hSemaphore, 1, NULL);
		return val;
	}
	else {
		std::cout << "r" << threadID << " " << -1 << std::endl;
		ReleaseSemaphore(hSemaphore, 1, NULL);
		return -1;
	}
}


SafeThreadQueue::~SafeThreadQueue() {
	CloseHandle(hSemaphore);
}