/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "ignite/impl/ignite_impl.h"

using namespace ignite::common::concurrent;
using namespace ignite::jni::java;

namespace ignite
{
    namespace impl
    {
        IgniteImpl::IgniteImpl(SharedPointer<IgniteEnvironment> env, jobject javaRef) :
            env(env),
            javaRef(javaRef),
            txImpl(),
            prjImpl()
        {
            txImpl.Init(common::Bind(this, &IgniteImpl::InternalGetTransactions));
            prjImpl.Init(common::Bind(this, &IgniteImpl::InternalGetProjection));
        }

        IgniteImpl::~IgniteImpl()
        {
            JniContext::Release(javaRef);
        }

        const char* IgniteImpl::GetName() const
        {
            return env.Get()->InstanceName();
        }

        const IgniteConfiguration& IgniteImpl::GetConfiguration() const
        {
            return env.Get()->GetConfiguration();
        }

        JniContext* IgniteImpl::GetContext()
        {
            return env.Get()->Context();
        }

        IgniteImpl::SP_IgniteBindingImpl IgniteImpl::GetBinding()
        {
            return env.Get()->GetBinding();
        }

        IgniteImpl::SP_ComputeImpl IgniteImpl::GetCompute()
        {
            cluster::SP_ClusterGroupImpl serversCluster = prjImpl.Get().Get()->ForServers();

            return serversCluster.Get()->GetCompute();
        }

        transactions::TransactionsImpl* IgniteImpl::InternalGetTransactions()
        {
            JniErrorInfo jniErr;

            jobject txJavaRef = env.Get()->Context()->ProcessorTransactions(javaRef, &jniErr);

            if (!txJavaRef)
            {
                IgniteError err;
                IgniteError::SetError(jniErr.code, jniErr.errCls, jniErr.errMsg, err);
                throw err;
            }

            return new transactions::TransactionsImpl(env, txJavaRef);
        }

        cluster::ClusterGroupImpl* IgniteImpl::InternalGetProjection()
        {
            JniErrorInfo jniErr;

            jobject clusterGroupJavaRef = env.Get()->Context()->ProcessorProjection(javaRef, &jniErr);

            if (!clusterGroupJavaRef)
            {
                IgniteError err;
                IgniteError::SetError(jniErr.code, jniErr.errCls, jniErr.errMsg, err);
                throw err;
            }

            return new cluster::ClusterGroupImpl(env, clusterGroupJavaRef);
        }
    }
}
